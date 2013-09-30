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
public abstract class InputStreamDictionary extends AbstractDictionary {

	/**
	 * Gets the input stream from which to read.
	 * 
	 * @return the input stream
	 */
	protected abstract InputStream getStream();

	@Override
	protected Collection<? extends String> loadDictionary() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					getStream()));
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