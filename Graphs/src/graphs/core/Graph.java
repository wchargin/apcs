package graphs.core;

/**
 * Any type of graph.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the type of value stored in the nodes of this graph
 * @param <N>
 *            the type of node in this graph
 * @param <E>
 *            the type of edge in this graph
 */
public interface Graph<T, N extends Node<T>, E extends Edge<T>> {

	/**
	 * Tests whether the given nodes are connected in the graph. This may have a
	 * different meaning depending on the graph and/or {@linkplain Edge}
	 * implementation.
	 * 
	 * @param n1
	 *            the first node
	 * @param n2
	 *            the second node
	 * @return whether the first node is connected to the second node
	 */
	public boolean connected(N n1, N n2);

	/**
	 * Tests whether the given node is contained in the graph.
	 * 
	 * @param node
	 *            the node to check
	 * @return whether the node is contained in the graph
	 */
	public boolean contains(N node);

	/**
	 * Performs the given operation on each node of the tree.
	 * 
	 * @param op
	 *            the operation to perform
	 */
	public void foreachNode(Operation<? super N> op);
	
	/**
	 * Performs the given operation on each edge of the tree.
	 * 
	 * @param op
	 *            the operation to perform
	 */
	public void foreachEdge(Operation<? super E> op);

}
