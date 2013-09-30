package graphs.wordladder.dictionary;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public abstract class AbstractDictionary implements DictionaryReader {

	/**
	 * Whether this dictionary has been loaded.
	 */
	private boolean initialized = false;

	/**
	 * The dictionary contents.
	 */
	private HashSet<String> dictionary;

	@Override
	public boolean contains(String word) {
		return dictionary.contains(word);
	}

	@Override
	public Iterator<String> iterator() {
		if (!initialized) {
			preloadDictionary();
		}
		return dictionary.iterator();
	}

	/**
	 * Loads the dictionary into a collection.
	 * 
	 * @return the dictionary contents
	 */
	protected abstract Collection<? extends String> loadDictionary();

	@Override
	public void preloadDictionary() {
		if (!initialized) {
			initialized = true;
			dictionary = new HashSet<>();
			for (String word : loadDictionary()) {
				// trim pre/post whitespace, lowercase, remove non-word characters
				dictionary.add(word.trim().toLowerCase().replaceAll("\\W", ""));
			}
		}
	}

}
