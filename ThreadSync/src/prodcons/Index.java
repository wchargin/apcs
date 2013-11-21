package prodcons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private Map<K, List<V>> map = new HashMap<>();

	/**
	 * Registers the given value onto the given key, not overwriting any
	 * previous values.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the new/additional value
	 */
	public void store(K key, V value) {
		List<V> vs = map.get(key);
		if (vs == null) {
			map.put(key, vs = new ArrayList<>());
		}
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
	public List<V> get(K key) {
		List<V> vs = map.get(key);
		return vs == null ? null : Collections.unmodifiableList(vs);
	}

}
