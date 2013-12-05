package prodcons;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import prodcons.ApplicationServer.Consumer;

/**
 * A simple consumer that prints out file contents for text-ish files.
 * 
 * @author William Chargin
 * 
 */
public class FileReadConsumer implements Consumer<Path> {

	// find txt, py, cpp, c, h, hpp
	private static final List<String> validEndings = Collections
			.unmodifiableList(Arrays
					.asList("txt", "py", "cpp", "c", "h", "hpp"));

	@Override
	public void consume(Path t) {
		for (String end : validEndings) {
			if (t.toString().toLowerCase().endsWith(end.toLowerCase())) {
				printFile(t);
				return;
			}
		}
	}

	private void printFile(Path t) {
		System.out.println("FILE ::: " + t);
		try {
			for (String line : Files.readAllLines(t, Charset.forName("UTF-8"))) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

}
