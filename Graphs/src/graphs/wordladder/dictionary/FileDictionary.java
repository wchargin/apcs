package graphs.wordladder.dictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A dictionary reader for a dictionary contained in a file.
 * 
 * @author William Chargin
 * 
 */
public class FileDictionary extends InputStreamDictionary {

	/**
	 * The file from which to read.
	 */
	private File file;

	/**
	 * Creates the dictionary with the given file.
	 * 
	 * @param file
	 *            the file containing the dictionary
	 */
	public FileDictionary(File file) {
		super();
		this.file = file;
	}

	@Override
	protected InputStream getStream() {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
