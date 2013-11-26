package prodcons;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

	public FileIndexingConsumer(Index<? super String, ? super Path> i,
			String... validEndings) {
		index = i;
		this.validEndings = Arrays.asList(validEndings);
	}

	@Override
	public void consume(Path t) {
		if (t == null) {
			return;
		}
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
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(t.toFile())))) {
			String line;
			while ((line = br.readLine()) != null) {
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
