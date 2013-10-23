package hash;

/**
 * A hash map that uses linear probing to resolve collisions.
 * 
 * @author William Chargin
 * 
 * @param <K>
 *            the type of keys stored in this map
 * @param <V>
 *            the type of values stored in this map
 */
public class LinearProbedHashMap<K, V> extends ProbedHashMap<K, V> {

	/**
	 * Creates the map with the given hash function and default load factor.
	 * 
	 * @param hasher
	 *            the hash function to use
	 */
	public LinearProbedHashMap(HashFunction<? super K> hasher) {
		super(hasher);
	}

	/**
	 * Creates the map with the given hash function.
	 * 
	 * @param hasher
	 *            the hash function to use
	 * @param loadFactor
	 *            the {@linkplain #loadFactor load factor} for the map
	 */
	public LinearProbedHashMap(HashFunction<? super K> hasher, float loadFactor) {
		super(hasher, loadFactor);
	}

	@Override
	protected int getProbeValue(int i) {
		return i;
	}

}
