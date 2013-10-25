package hash;

/**
 * A hash map that uses quadratic probing to resolve collisions.
 * 
 * @author William Chargin
 * 
 * @param <K>
 *            the type of keys stored in this map
 * @param <V>
 *            the type of values stored in this map
 */
public class QuadraticProbedHashMap<K, V> extends PolynomialProbedHashMap<K, V> {

	/**
	 * Creates the map with the given hash function and default load factor.
	 * 
	 * @param hasher
	 *            the hash function to use
	 */
	public QuadraticProbedHashMap(HashFunction<? super K> hasher) {
		super(hasher, 2, 0, 1);
	}

	/**
	 * Creates the map with the given hash function and load factor.
	 * 
	 * @param hasher
	 *            the hash function to use
	 * @param loadFactor
	 *            the {@linkplain #loadFactor load factor} for the map
	 */
	public QuadraticProbedHashMap(HashFunction<? super K> hasher,
			float loadFactor) {
		super(hasher, loadFactor, 2, 0, 1);
	}

}
