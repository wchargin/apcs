package graphs.wordladder.dictionary;

/**
 * A dictionary reader interface.
 * 
 * @author William Chargin
 * 
 */
public interface DictionaryReader extends Iterable<String> {

	/**
	 * Preloads the dictionary into memory.
	 */
	public void preloadDictionary();

	/**
	 * Tests whether a given word is contained in the dictionary.
	 * 
	 * @param word
	 *            the word to check
	 * @return {@code true} if the word is in the dictionary, or {@code false}
	 *         if it is not
	 */
	public boolean contains(String word);

}
