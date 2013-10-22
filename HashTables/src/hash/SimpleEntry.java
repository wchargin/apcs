package hash;

import java.util.Map.Entry;

/**
 * A basic entry implementation that acts as a pure data structure.
 * 
 * @author William Chargin
 * 
 * @param <K>
 *            the type of keys stored in this entry
 * @param <V>
 *            the type of values stored in this entry
 */
public class SimpleEntry<K, V> implements Entry<K, V> {

	/**
	 * The key stored in this entry.
	 */
	private K key;

	/**
	 * The value stored in this entry.
	 */
	private V value;

	/**
	 * Creates the entry with the given key and value.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public SimpleEntry(K key, V value) {
		super();
		this.key = key;
		this.value = value;
	}

	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public V setValue(V value) {
		V old = this.value;
		this.value = value;
		return old;
	}

	@Override
	public String toString() {
		return "Entry [key=" + key + ", value=" + value + "]";
	}

}
