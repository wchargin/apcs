package prodcons;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import prodcons.ApplicationServer.Producer;

/**
 * An object that produces paths by walking a file tree.
 * 
 * @author William Chargin
 * 
 */
public class FileTreeProducer implements Producer<Path> {

	/**
	 * The queue of walked but not "produced" paths.
	 */
	private BlockingQueue<Path> queue = new LinkedBlockingQueue<>();

	/**
	 * Creates a file tree producer walking the given subtree.
	 * 
	 * @param root
	 *            the root of the subtree to walk
	 */
	public FileTreeProducer(final Path root) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
						@Override
						public FileVisitResult visitFile(Path file,
								BasicFileAttributes attrs) throws IOException {
							try {
								queue.put(file);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							return super.visitFile(file, attrs);
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public Path produce() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			return null;
		}
	}

}
