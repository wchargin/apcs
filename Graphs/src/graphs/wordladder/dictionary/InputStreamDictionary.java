package graphs.wordladder.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A dictionary that reads from an input stream.
 * 
 * @author William Chargin
 * 
 */
public class InputStreamDictionary extends AbstractDictionary {

	/**
	 * The stream to read from.
	 */
	private InputStream stream;

	public InputStreamDictionary(InputStream in) {
		stream = in;
	}

	@Override
	protected Collection<? extends String> loadDictionary() {
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(stream));
			List<String> contents = new ArrayList<>();
			String line;
			while ((line = br.readLine()) != null) {
				contents.add(line);
			}
			return contents;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}