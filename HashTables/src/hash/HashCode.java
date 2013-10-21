package hash;

/**
 * A hash function that delegates to the standard {@link Object#hashCode()}
 * method.
 * 
 * @author William Chargin
 * 
 */
public class HashCode implements HashFunction<Object> {

	@Override
	public int hash(Object t) {
		return t.hashCode();
	}

}
