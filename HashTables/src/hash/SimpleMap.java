package hash;

import java.util.Map.Entry;
import java.util.Set;

/**
 * A subset of the {@link java.util.Map} interface. Maps unique keys to values.
 * 
 * @author William Chargin
 * 
 * @param <K>
 *            the type of keys stored in this map
 * @param <V>
 *            the type of values stored in this map
 */
public interface SimpleMap<K, V> {

	/**
	 * Removes all entries from the map.
	 */
	public void clear();

	/**
	 * Tests whether the map contains the given key.
	 * 
	 * @param key
	 *            the key to check
	 * @return {@code true} if the key is in the map, or {@code false} if it is
	 *         not
	 */
	public boolean containsKey(K key);

	/**
	 * Tests whether the map contains the given value.
	 * 
	 * @param value
	 *            the value to check
	 * @return {@code true} if the value is held by some key in the map, or
	 *         {@code false} if it is not
	 */
	public boolean containsValue(V value);

	/**
	 * Gets a set of all the entries in the map.
	 * 
	 * @return a set of all the key-value pairs
	 */
	public Set<? extends Entry<K, V>> entrySet();

	/**
	 * Gets the key from the map.
	 * 
	 * @param key
	 *            the key
	 * @return the value associated with this key
	 */
	public V get(K key);

	/**
	 * Puts this key-value pair into the map.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @return the old value, if any
	 */
	public V put(K key, V value);

	/**
	 * Removes the key from the map.
	 * 
	 * @param key
	 *            the key to remove
	 * @return the old value, if any
	 */
	public V remove(K key);

	/**
	 * Gets the size (number of keys) in this map.
	 * 
	 * @return the size of the map
	 */
	public int size();

}
