package hash;

/**
 * An abstract map that allows for a hash function to be passed to the
 * constructor.
 * 
 * @author William Chargin
 * 
 * @param <K>
 *            the type of keys stored in this map
 * @param <V>
 *            the type of values stored in this map
 */
public abstract class AbstractHashMap<K, V> implements SimpleMap<K, V> {

	/**
	 * Tests if two objects are both {@code null} or both
	 * {@link Object#equals(Object) equal}.
	 * 
	 * @param o1
	 *            the first object
	 * @param o2
	 *            the second object
	 * @return if both objects are {@code null} or {@code o1.equals(o2)}
	 */
	protected static boolean equals(Object o1, Object o2) {
		return (o1 == null) ? (o2 == null) : o1.equals(o2);
	}

	/**
	 * The hash function used in this map.
	 */
	protected final HashFunction<? super K> hasher;

	/**
	 * Creates the map with the given hash function.
	 * 
	 * @param hasher
	 *            the hash function to use
	 */
	public AbstractHashMap(HashFunction<? super K> hasher) {
		super();
		this.hasher = hasher;
	}

}
