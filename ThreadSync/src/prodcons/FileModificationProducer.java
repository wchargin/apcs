package prodcons;

import static java.nio.file.StandardWatchEventKinds.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import prodcons.ApplicationServer.Producer;

public class FileModificationProducer implements Producer<Path> {

	public FileModificationProducer(final FileIndex index) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try (WatchService ws = FileSystems.getDefault()
						.newWatchService()) {
					index.getRoot().register(ws);
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
							if (kind == ENTRY_CREATE) {
								index.storeFile(file);
							} else if (kind == ENTRY_DELETE) {
								index.removeFile(file);
							} else if (kind == ENTRY_MODIFY) {
								index.updateFile(file);
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

		return null;
	}

}
