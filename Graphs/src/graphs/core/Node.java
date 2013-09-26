package graphs.core;

/**
 * A node in a graph.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the type of value stored in this node
 */
public interface Node<T> {

	/**
	 * Gets the value of this node.
	 * 
	 * @return the value
	 */
	public T getValue();

}
