package hash;

import java.util.Random;

/**
 * A hash function that delegates uses a seeded random number generator chained
 * with another hash implementation.
 * 
 * @author William Chargin
 * 
 */
public class SeededHash<T> implements HashFunction<T> {

	/**
	 * The function used for calculating a random seed.
	 */
	private final HashFunction<? super T> chain;

	/**
	 * The seed offset for the random number generator.
	 */
	private final int offset;

	/**
	 * Creates the hash with the given chain and offset.
	 * 
	 * @param chain
	 *            the hash function to chain to
	 * @param offset
	 *            the offset for the random seed
	 */
	private SeededHash(HashFunction<? super T> chain, int offset) {
		this.chain = chain;
		this.offset = offset;
	}

	/**
	 * Creates a seeded hash, chaining with a {@link HashCode} hash function and
	 * using the given offset.
	 * 
	 * @param offset
	 *            the offset for the random seed
	 */
	public static SeededHash<Object> fromHashCode(int offset) {
		return new SeededHash<Object>(new HashCode(), offset);
	}

	/**
	 * Creates a seeded hash, chaining with the given hash function and using
	 * the given offset.
	 * 
	 * @param chain
	 *            the hash function to chain to
	 * @param offset
	 *            the offset for the random seed
	 */
	public static <T> SeededHash<T> fromChained(HashFunction<? super T> chain,
			int offset) {
		return new SeededHash<T>(chain, offset);
	}

	@Override
	public int hash(T t) {
		return new Random(chain.hash(t) + offset).nextInt();
	}

}
