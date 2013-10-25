package hash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A bloom filter for probabilistic data storage. The filter may incorrectly
 * assert that an element is in the set, but will never incorrectly assert that
 * it is not.
 * 
 * @author William Chargin
 * 
 * @param <E>
 *            the type of elements stored in this filter
 */
public class BloomFilter<E> {

	/**
	 * Creates a list of independent hash functions.
	 * 
	 * @param count
	 *            the length of the list to be generated
	 * @return a list of functions
	 */
	private static <E> List<HashFunction<? super E>> generateHashFunctions(
			int count) {
		Random r = new Random(Double.doubleToLongBits(Math.random()));
		List<HashFunction<? super E>> list = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			list.add(SeededHash.fromHashCode(r.nextInt()));
		}
		return list;
	}

	/**
	 * The bitmask for this filter.
	 */
	private final Bitmask mask;

	/**
	 * The list of hash functions used for this bloom filter.
	 */
	private final List<HashFunction<? super E>> hashes;

	/**
	 * Constructs a Bloom filter of the given size with the given number of hash
	 * functions. Hashes will be automatically generated.
	 * 
	 * @param size
	 *            the size of the filter (number of bits <em>m</em>)
	 * @param hashFunctionCount
	 *            the number of hashes to use (length <em>k</em>)
	 */
	public BloomFilter(int size, int hashFunctionCount) {
		this(size, BloomFilter.<E> generateHashFunctions(hashFunctionCount));
	}

	/**
	 * Constructs a Bloom filter of the given size with the given family of
	 * hashes.
	 * 
	 * @param size
	 *            the size of the filter (number of bits <em>m</em>)
	 * @param hashes
	 *            the list of hashes to use (length <em>k</em>)
	 */
	public BloomFilter(int size, List<HashFunction<? super E>> hashes) {
		super();
		this.mask = new Bitmask(size);
		this.hashes = Collections.unmodifiableList(hashes);
	}

	/**
	 * Tests if an element is in the set.
	 * 
	 * @param e
	 *            the element to check
	 * @return {@code true} if the element might be in the set, or {@code false}
	 *         if it is definitely not
	 */
	public boolean contains(E e) {
		for (HashFunction<? super E> hash : hashes) {
			if (!mask.get(getPosition(e, hash))) {
				return false;
			}
		}
		return true;
	}

	private int getPosition(E e, HashFunction<? super E> hash) {
		return e == null ? 0 : Math.abs(hash.hash(e)) % mask.size();
	}

	/**
	 * Puts the given element into the Bloom filter.
	 * 
	 * @param e
	 *            the element
	 */
	public void put(E e) {
		for (HashFunction<? super E> hash : hashes) {
			mask.set(getPosition(e, hash));
		}
	}

}
