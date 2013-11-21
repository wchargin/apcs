package prodcons;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import prodcons.ApplicationServer.Consumer;

/**
 * A consumer that stores all words in a file and its filepath into an index.
 * 
 * @author William Chargin
 * 
 */
public class FileIndexingConsumer implements Consumer<Path> {

	// find txt, py, cpp, c, h, hpp
	private static final List<String> validEndings = Collections
			.unmodifiableList(Arrays
					.asList("txt", "py", "cpp", "c", "h", "hpp"));
	private Index<? super String, ? super Path> index;

	public FileIndexingConsumer(Index<? super String, ? super Path> i) {
		index = i;
	}

	@Override
	public void consume(Path t) {
		for (String end : validEndings) {
			if (t.toString().toLowerCase().endsWith("." + end.toLowerCase())) {
				processFile(t);
				return;
			}
		}
	}

	private void processFile(Path t) {
		Set<String> words = new HashSet<>();
		// start with filename
		for (String word : t.toString().split("\\W")) {
			words.add(word.toLowerCase());
		}
		try {
			for (String line : Files.readAllLines(t, Charset.forName("UTF-8"))) {
				for (String word : line.split("\\W")) {
					words.add(word.toLowerCase());
				}
			}
		} catch (IOException e) {
			System.err.println(t);
			e.printStackTrace();
		}
		for (String word : words) {
			index.store(word, t);
		}
	}

}
