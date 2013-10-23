package hash;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A hash map that uses probing (such as linear or quadratic probing) to resolve
 * collisions. Subclasses must implement the {@link #getProbeValue(int)} method.
 * 
 * @author William Chargin
 * 
 * @param <K>
 *            the type of keys stored in this map
 * @param <V>
 *            the type of values stored in this map
 */
public abstract class ProbedHashMap<K, V> extends
		ArrayHashMap<K, V, Entry<K, V>> {

	/**
	 * The size of this array (number of key-value pairs), maintained for easy
	 * computation.
	 */
	private int size;

	/**
	 * The entry used to indicate a null value.
	 */
	private final Entry<K, V> empty = new SimpleEntry<K, V>(null, null);

	/**
	 * Creates the map with the given hash function and default load factor.
	 * 
	 * @param hasher
	 *            the hash function to use
	 */
	public ProbedHashMap(HashFunction<? super K> hasher) {
		super(hasher);
	}

	/**
	 * Creates the map with the given hash function and load factor.
	 * 
	 * @param hasher
	 *            the hash function to use
	 * @param loadFactor
	 *            the {@linkplain #loadFactor load factor} for the map
	 */
	public ProbedHashMap(HashFunction<? super K> hasher, float loadFactor) {
		super(hasher, loadFactor);
	}

	@Override
	protected int calculatePosition(K key) {
		while (true) {
			int calculated = calculatePositionPassive(key);
			if (calculated >= 0) {
				// valid position found
				return calculated;
			} // else
				// no position found
				// remap and try again
			remapDouble();
		}
	}

	/**
	 * Calculates the array position for a given key. Unlike
	 * {@link #calculatePosition(Object)}, this will <em>not</em> force a remap
	 * if the key has no valid position; instead, it will simply return
	 * {@code -1}.
	 * 
	 * @param key
	 *            the key whose position to find
	 * @return the position, or {@code -1} if the key is not in the map and
	 *         there is no space
	 */
	private int calculatePositionPassive(K key) {
		final int originalPos = super.calculatePosition(key);
		int pos = originalPos;

		// check the position for validity at least once, and until we find
		// it or loop back to the beginning
		//
		// validity: slot is null or contains the same key
		for (int i = 0; originalPos != pos || i == 0; i++) {
			if (list[pos] == null || list[pos] == empty
					|| equals(list[pos].getKey(), key)) {
				// this is the correct position
				return pos;
			}
			pos = getProbeValue(i) % list.length;
		}

		// we looped around; no valid position
		// fail
		return -1;
	}

	@Override
	public void clear() {
		Arrays.fill(list, null);
		size = 0;
	}

	@Override
	public boolean containsKey(K key) {
		for (Entry<K, V> entry : list) {
			if (isValidEntry(entry) && equals(entry.getKey(), key)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsValue(V value) {
		for (Entry<K, V> entry : list) {
			if (isValidEntry(entry) && equals(entry.getValue(), value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Set<? extends Entry<K, V>> entrySet() {
		HashSet<Entry<K, V>> entries = new HashSet<>();
		for (Entry<K, V> entry : list) {
			if (isValidEntry(entry)) {
				// don't count the empty entry
				// this also fixes a potential problem in #remap(int)
				entries.add(entry);
			}
		}
		return entries;
	}

	@Override
	public V get(K key) {
		final int pos = calculatePositionPassive(key);
		if (pos < 0) {
			return null;
		}
		final Entry<K, V> at = list[pos];
		return at == null ? null : at.getValue();
	}

	/**
	 * Gets the probe offset for a given iteration. For example, the offset for
	 * a quadratic probe might be {@code 0} when {@code i} = 0, {@code 1} when
	 * {@code i} = 1, {@code 4} when {@code i} = 2, etc.
	 * 
	 * @param i
	 *            the iteration number
	 * @return the probe offset
	 */
	protected abstract int getProbeValue(int i);

	@SuppressWarnings("unchecked")
	@Override
	protected void initializeList(int size) {
		list = (SimpleEntry[]) Array.newInstance(SimpleEntry.class, size);
		clear();
	}

	/**
	 * Tests if the current entry is non-{@code null} and non-{@link #empty}.
	 * 
	 * @param entry
	 *            the entry to test
	 * @return {@code true} if it is valid, or {@code false} if it is
	 *         {@code null} or {@link #empty}
	 */
	private boolean isValidEntry(Entry<K, V> entry) {
		return entry != null && entry != empty;
	}

	@Override
	public V put(K key, V value) {
		// the calculatePosition method will force remap until we have validity
		int pos = calculatePosition(key);
		Entry<K, V> entry = list[pos];
		if (entry != null) {
			return entry.setValue(value);
		} else {
			list[pos] = new SimpleEntry<K, V>(key, value);
			size++;
			remapIfNecessary();
			return null;
		}
	}

	@Override
	public V remove(K key) {
		int pos = calculatePositionPassive(key);
		if (pos < 0) {
			return null;
		}
		final Entry<K, V> at = list[pos];
		if (at == null || at == empty) {
			// not in map
			return null;
		} else {
			V value = at.getValue();
			list[pos] = empty;
			size--;
			return value;
		}
	}

	@Override
	public int size() {
		return size;
	}

}
