package prodcons;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import prodcons.ApplicationServer.Consumer;
import prodcons.ApplicationServer.Producer;

/**
 * A specialized {@link Index} specifically for file indexing that contains
 * additional, specific features.
 * 
 * @author William Chargin
 * 
 */
public class FileIndex extends Index<String, Path> {

	/**
	 * A consumer that stores all words in a file and its filepath into the
	 * index.
	 * 
	 * @author William Chargin
	 * 
	 */
	public class FileIndexingConsumer implements Consumer<Path> {
		@Override
		public void consume(Path t) {
			if (isValidFile(t)) {
				storeFile(t);
			}
		}
	}

	/**
	 * Tests if a given file is valid.
	 * 
	 * @param t
	 *            the file to check
	 * @return whether the file is valid for parsing/indexing
	 */
	private boolean isValidFile(Path t) {
		if (t == null) {
			return false;
		}
		final String str = t.toString().toLowerCase();
		for (String end : validEndings) {
			if (str.endsWith("." + end.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public class FileModificationProducer implements Producer<Path> {

		private BlockingQueue<Path> q = new LinkedBlockingQueue<>();

		public FileModificationProducer() {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try (WatchService ws = FileSystems.getDefault()
							.newWatchService()) {
						getRoot().register(ws);
						while (true) {
							WatchKey key;
							try {
								key = ws.take();
							} catch (InterruptedException e) {
								e.printStackTrace();
								return;
							}

							for (WatchEvent<?> event : key.pollEvents()) {
								Kind<?> kind = event.kind();
								if (kind == OVERFLOW) {
									continue;
								}

								// If it's not OVERFLOW it's the path
								@SuppressWarnings("unchecked")
								WatchEvent<Path> pevent = (WatchEvent<Path>) event;

								
								Path file = pevent.context();
								System.out.println(file);
								if (kind == ENTRY_CREATE) {
									// consumers gonna consume
								} else if (kind == ENTRY_DELETE) {
									removeFile(file);
								} else if (kind == ENTRY_MODIFY) {
									removeFile(file);
									try {
										q.put(file);
									} catch (InterruptedException e) {
										continue;
									}
								}

								// reset and check status
								boolean keyStillValid = key.reset();
								if (!keyStillValid) {
									break;
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
				}
			});
		}

		@Override
		public Path produce() {
			try {
				return q.take();
			} catch (InterruptedException e) {
				return null;
			}
		}

	}

	/**
	 * The root directory.
	 */
	private Path root;

	/**
	 * The list of valid endings.
	 */
	private String[] validEndings;

	/**
	 * Creates the index with the given root directory.
	 * 
	 * @param root
	 *            the root directory
	 */
	public FileIndex(Path root, String... validEndings) {
		this.root = root;
		this.validEndings = validEndings;
	}

	/**
	 * Creates a producer to crawl the file tree.
	 * 
	 * @return a new producer
	 */
	public FileTreeProducer createProducer() {
		return new FileTreeProducer(root);
	}

	/**
	 * Creates a producer to watch for file changes.
	 * 
	 * @return a new producer
	 */
	public FileModificationProducer createWatcher() {
		return this.new FileModificationProducer();
	}

	/**
	 * Creates a consumer to index items.
	 * 
	 * @return a new consumer
	 */
	public FileIndexingConsumer createConsumer() {
		return this.new FileIndexingConsumer();
	}

	/**
	 * Gets the root directory of this index.
	 * 
	 * @return the root directory
	 */
	public Path getRoot() {
		return root;
	}

	public void storeFile(Path file) {
		Set<String> words = new HashSet<>();
		// start with filename
		for (String word : file.toString().split("\\W")) {
			words.add(word.toLowerCase());
		}
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file.toFile())))) {
			String line;
			while ((line = br.readLine()) != null) {
				for (String word : line.split("\\W")) {
					words.add(word.toLowerCase());
				}
			}
		} catch (IOException e) {
			System.err.println(file);
			e.printStackTrace();
		}
		for (String word : words) {
			store(word, file);
		}
	}

	public void removeFile(Path file) {
		for (Set<Path> paths : map.values()) {
			paths.remove(file);
		}
	}

	public void updateFile(Path file) {
		removeFile(file);
		storeFile(file);
	}

}
