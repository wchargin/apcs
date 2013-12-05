package prodcons;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This is an implementation of an <a
 * href="http://en.wikipedia.org/wiki/Inverted_index">inverted index</a>. For
 * example, to index words in files, you could use an
 * {@code Index<String, Path>}.
 * 
 * @author William Chargin
 * 
 * @param <K>
 *            the search keys stored in the index
 * @param <V>
 *            the result values stored in the keys
 */
public class Index<K, V> {

	/**
	 * The map used for storage.
	 */
	protected Map<K, Set<V>> map = Collections
			.synchronizedMap(new HashMap<K, Set<V>>());

	/**
	 * The set of all values used for counting.
	 */
	private Set<V> allValues = Collections.synchronizedSet(new HashSet<V>());

	/**
	 * Registers the given value onto the given key, not overwriting any
	 * previous values.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the new/additional value
	 */
	public synchronized void store(K key, V value) {
		Set<V> vs = map.get(key);
		if (vs == null) {
			map.put(key, vs = new HashSet<>());
		}
		allValues.add(value);
		vs.add(value);
	}

	/**
	 * Gets all values previously {@linkplain #store(Object, Object) stored}
	 * onto the given key.
	 * 
	 * @param key
	 *            the key to query
	 * @return all matching values
	 */
	public synchronized Set<V> get(K key) {
		Set<V> vs = map.get(key);
		if (vs == null) {
			return null;
		}
		Set<V> syncedSet = Collections.synchronizedSet(vs);
		synchronized (syncedSet) {
			return new HashSet<>(syncedSet);
		}
	}

	/**
	 * Gets the number of unique values in this index.
	 * 
	 * @return the number of unique values
	 */
	public int getValueSize() {
		return allValues.size();
	}

}
