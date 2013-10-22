package hash;

import java.util.Map;
import java.util.Set;

/**
 * Any hash-based map that is backed by an array. This provides load-factor and
 * remapping operations.
 * 
 * @author William Chargin
 * 
 * @param <K>
 *            the type of keys stored in this map
 * @param <V>
 *            the type of values stored in this map
 * @param <E>
 *            the type of entries stored in the backing array
 */
public abstract class ArrayHashMap<K, V, E> extends AbstractHashMap<K, V> {

	/**
	 * The default load factor for a map.
	 */
	protected static final float DEFAULT_LOAD_FACTOR = 0.75f;

	/**
	 * The default size of the list.
	 */
	protected static final int DEFAULT_SIZE = 7;

	/**
	 * The load factor for this hash map.
	 */
	protected final float loadFactor;

	/**
	 * The list of entries.
	 */
	protected E[] list;

	/**
	 * Creates the map with the given hash function and default load factor.
	 * 
	 * @param hasher
	 *            the hash function to use
	 */
	public ArrayHashMap(HashFunction<? super K> hasher) {
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
	public ArrayHashMap(HashFunction<? super K> hasher, float loadFactor) {
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
		return (float) size() / list.length;
	}

	/**
	 * Initializes the internal list to be a new list of the given size.
	 * 
	 * @param size
	 *            the size of this list
	 */
	protected abstract void initializeList(int size);

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
	protected void remapIfNecessary() {
		if (calculateLoadFactor() > loadFactor) {
			remap(list.length * 2);
		}
	}

}
