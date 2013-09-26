package graphs.core;

/**
 * An edge in a graph.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the type of value in the nodes connected by this edge
 */
public interface Edge<T> {

	/**
	 * Determines if this edge is said to "connect" the given nodes. The meaning
	 * of this method may depend on the edge implementation.
	 * 
	 * @param a
	 *            the first node in question
	 * @param b
	 *            the second node in question
	 * @return whether this connects the first node to the second node
	 */
	public boolean connects(Node<T> a, Node<T> b);

}
