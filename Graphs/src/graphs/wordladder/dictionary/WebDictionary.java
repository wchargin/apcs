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
	 * The URL for this dictionary.
	 */
	private final URL url;

	/**
	 * Creates the dictionary with the given URL.
	 * 
	 * @param url
	 *            the URL containing the dictionary
	 */
	public WebDictionary(URL url) {
		super();
		this.url = url;
	}

	@Override
	protected InputStream getStream() {
		try {
			return url.openStream();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}