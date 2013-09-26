package graphs.core;

/**
 * An operation to be performed on some type of element.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the type of element accepted by this operation
 */
public interface Operation<T> {

	/**
	 * Invokes the operation on the given element.
	 * 
	 * @param t
	 *            the element on which the operation should be invoked
	 */
	public void invoke(T t);

}
