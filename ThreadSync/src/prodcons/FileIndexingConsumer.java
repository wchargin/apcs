package prodcons;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
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

	private final List<String> validEndings;
	private Index<? super String, ? super Path> index;
	private Charset charset;

	public FileIndexingConsumer(Index<? super String, ? super Path> i, Charset charset, String...validEndings) {
		index = i;
		this.validEndings=Arrays.asList(validEndings);
		this.charset = charset;
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
			for (String line : Files.readAllLines(t, charset)) {
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
