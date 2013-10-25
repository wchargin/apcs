package hash;

/**
 * A hash function that converts objects to hashes. This should follow the
 * general contract of {@link Object#hashCode()}.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the type of objects hashed by this function
 */
public interface HashFunction<T> {

	/**
	 * Calculates the hash of the given object.
	 * 
	 * @param t
	 *            the object to hash
	 * @return the hash
	 */
	public int hash(T t);

}
