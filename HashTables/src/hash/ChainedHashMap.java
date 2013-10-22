package hash;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A hash map that uses chaining to resolve collisions.
 * 
 * @author William Chargin
 * 
 * @param <K>
 *            the type of keys stored in this map
 * @param <V>
 *            the type of values stored in this map
 */
public class ChainedHashMap<K, V> extends AbstractHashMap<K, V> {

	/**
	 * The entries stored in a chained map. In addition to keys and values,
	 * these entries also store a {@link #next} pointer.
	 * 
	 * @author William Chargin
	 * 
	 */
	private class Entry extends SimpleEntry<K, V> {

		/**
		 * The next entry in this chain, or {@code null} if this is the end of
		 * the road.
		 */
		private Entry next;

		/**
		 * Creates an entry with the given key and value.
		 * 
		 * @param key
		 *            the key
		 * @param value
		 *            the value
		 */
		public Entry(K key, V value) {
			super(key, value);
		}

	}

	/**
	 * The list of entries.
	 */
	private Entry[] list;

	/**
	 * The default size of the list.
	 */
	private static final int DEFAULT_SIZE = 7;

	/**
	 * The default load factor for a map.
	 */
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;

	/**
	 * The load factor for this hash map.
	 */
	private final float loadFactor;

	/**
	 * The size of this table, maintained for easy computations.
	 */
	private int size = 0;

	/**
	 * Creates the map with the given hash function.
	 * 
	 * @param hasher
	 *            the hash function to use
	 */
	public ChainedHashMap(HashFunction<? super K> hasher) {
		this(hasher, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * Creates the map with the given hash function.
	 * 
	 * @param hasher
	 *            the hash function to use
	 * @param loadFactor
	 *            the {@linkplain #loadFactor load factor} for the map
	 */
	public ChainedHashMap(HashFunction<? super K> hasher, float loadFactor) {
		super(hasher);

		initializeList(DEFAULT_SIZE);
		this.loadFactor = loadFactor;
	}

	/**
	 * Calculates the current actual load factor of the map.
	 * 
	 * @return the current load factor
	 */
	private float calculateLoadFactor() {
		return (float) size / list.length;
	}

	/**
	 * Calculates the array position for a given key. This takes the modulus of
	 * the hash function with the list length.
	 * 
	 * @param key
	 *            the key
	 * @return the array position
	 */
	private int calculatePosition(K key) {
		return (int) ((key != null ? ((long) hasher.hash(key) + Integer.MAX_VALUE)
				: 0) % list.length);
	}

	@Override
	public void clear() {
		Arrays.fill(list, null);
		size = 0;
	}

	@Override
	public boolean containsKey(K key) {
		int pos = calculatePosition(key);
		Entry t = list[pos];
		while (t != null) {
			if (equals(key, t.getKey())) {
				return true;
			}
			t = t.next;
		}
		return false;
	}

	@Override
	public boolean containsValue(V value) {
		for (Entry e : list) {
			while (e != null) {
				if (equals(value, e.getValue())) {
					return true;
				}
				e = e.next;
			}
		}
		return false;
	}

	@Override
	public Set<? extends Map.Entry<K, V>> entrySet() {
		// is it okay to use the built-in HashSet (which relies on the built-in
		// HashMap) in my HashMap implementation?
		Set<Entry> entries = new HashSet<Entry>();
		for (Entry e : list) {
			while (e != null) {
				entries.add(e);
				e = e.next;
			}
		}
		return entries;
	}

	@Override
	public V get(K key) {
		int pos = calculatePosition(key);
		Entry t = list[pos];
		while (t != null && t.getKey() != key) {
			t = t.next;
		}
		return t == null ? null : t.getValue();
	}

	/**
	 * Initializes the internal list to be a new list of the given size. This
	 * method uses unchecked generics with the
	 * {@link Array#newInstance(Class, int)} method.
	 * 
	 * @param size
	 *            the size of this list
	 */
	@SuppressWarnings("unchecked")
	private void initializeList(int size) {
		list = (Entry[]) Array.newInstance(Entry.class, size);
		clear();
	}

	@Override
	public V put(K key, V value) {
		int pos = calculatePosition(key);
		Entry t = list[pos];

		if (t == null) {
			// first in chain; set up list
			list[pos] = new Entry(key, value);
			size++;
			remapIfNecessary();
			return null;
		}

		if (equals(key, t.getKey())) {
			// found at the first link in the chain
			return t.setValue(value);
		}

		// find the end of the list, or check for replacement
		while (t.next != null) {
			if (equals(key, t.getKey())) {
				// found an existing key; replace value
				return t.setValue(value);
			}
			t = t.next;
		}

		// reached the end of the chain; append
		t.next = new Entry(key, value);
		size++;
		remapIfNecessary();
		return null;
	}

	/**
	 * Reinitializes this map with a new size to remove collisions. This method
	 * should be run when the {@linkplain #calculateLoadFactor() current load
	 * factor} exceeds the {@linkplain #loadFactor specified load factor}.
	 * 
	 * @param newSize
	 *            the new size for the internal list
	 */
	private void remap(int newSize) {
		final Set<? extends Map.Entry<K, V>> oldList = entrySet();

		initializeList(newSize);
		for (Map.Entry<K, V> entry : oldList) {
			put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Remaps this map's internal list, doubling the current size, if the
	 * {@linkplain #calculateLoadFactor() current load factor} exceeds the
	 * {@linkplain #loadFactor specified load factor}.
	 */
	private void remapIfNecessary() {
		if (calculateLoadFactor() > loadFactor) {
			remap(list.length * 2);
		}
	}

	@Override
	public V remove(K key) {
		int pos = calculatePosition(key);
		Entry t = list[pos];
		if (t == null) {
			// nothing at this hash position
			return null;
		}
		if (equals(key, t.getKey())) {
			// found at the first link in the chain
			list[pos] = t.next;
			size--;
			return t.getValue();
		}

		Entry previous = null;
		while (t != null && t.getKey() != key) {
			previous = t;
			t = t.next;
		}
		if (t == null) {
			// not found in map
			return null;
		} else {
			previous.next = t.next;
			size--;
			return t.getValue();
		}
	}

	@Override
	public int size() {
		return size;
	}

}
