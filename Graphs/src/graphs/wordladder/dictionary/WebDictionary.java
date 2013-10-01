package graphs.wordladder.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * A dictionary that reads from a URL.
 * 
 * @author William Chargin
 * 
 */
public class WebDictionary extends InputStreamDictionary {

	/**
	 * Creates the dictionary with the given URL.
	 * 
	 * @param url
	 *            the URL containing the dictionary
	 */
	public WebDictionary(URL url) {
		super(getStream(url));
	}

	private static InputStream getStream(URL url) {
		try {
			return url.openStream();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}