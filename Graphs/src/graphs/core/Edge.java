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
	 * of this method may depend on the edge implementation, but it is required
	 * that a call to this method with the result of {@link #getHead()} as the
	 * first argument and the result of {@link #getTail()} as the second
	 * argument return {@code true}.
	 * 
	 * @param a
	 *            the first node in question
	 * @param b
	 *            the second node in question
	 * @return whether this connects the first node to the second node
	 */
	public boolean connects(Node<T> a, Node<T> b);

	/**
	 * Gets the head node.
	 * 
	 * @return the head node of this edge
	 */
	public Node<T> getHead();

	/**
	 * Gets the tail node.
	 * 
	 * @return the tail node of this edge
	 */
	public Node<T> getTail();

}
