package prodcons;

import java.nio.file.Path;

import prodcons.ApplicationServer.Consumer;

/**
 * A simple consumer that prints out file names.
 * 
 * @author William Chargin
 * 
 */
public class FileNameConsumer implements Consumer<Path> {

	@Override
	public void consume(Path t) {
		System.out.println(t);
	}

}
