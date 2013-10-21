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
