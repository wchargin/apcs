package graphs.core;

/**
 * A graph that can be modified. Mutable graphs support some basic graph
 * operations, such as {@link #add(Node)}, {@link #remove(Node)},
 * {@link #link(Node, Node)}, and
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
public interface MutableGraph<T, N extends Node<T>, E extends Edge<T>>
		extends Graph<T, N, E> {

	/**
	 * Adds the node to the graph. If the node is already in the graph, no
	 * action is taken and no error is thrown.
	 * 
	 * @param node
	 *            the node to add
	 */
	public void add(N node);

	/**
	 * Removes every node that is not connected to any other node (any isolated
	 * node). More precisely, this method removes every node {@code n} such that
	 * there exists no edge {@code e} in the graph for which
	 * {@link Edge#connects(Node, Node) e.connects(n, m)} or
	 * {@link Edge#connects(Node, Node) e.connects(m, n)} is true for any node
	 * {@code m} in the graph.
	 */
	public void clean();

	/**
	 * Links the given nodes with an edge {@code e} such that
	 * {@link Edge#connects(Node, Node) e.connects(n1, n2)} is {@code true}, and
	 * therefore causes immediately subsequent calls to
	 * {@link #connected(Node, Node) connected(n1, n2)} return {@code true} as
	 * well.
	 * 
	 * @param n1
	 *            the first node to connect
	 * @param n2
	 *            the second node to connect
	 */
	public void link(N n1, N n2);

	/**
	 * Removes the given node from the graph and removes all edges to which it
	 * is connected from the graph.
	 * 
	 * @param node
	 *            the node to remove
	 */
	public void remove(N node);

	/**
	 * Removes every edge {@code e} for which there exists some node {@code m}
	 * in the graph such that {@link Edge#connects(Node, Node) e.connects(n, m)}
	 * or {@link Edge#connects(Node, Node) e.connects(m, n)} is {@code true}.
	 * This is functionally equivalent to, but may be faster than, calling:
	 * 
	 * <pre>
	 * unlink(n, null);
	 * unlink(null, n);
	 * </pre>
	 * 
	 * @see #unlink(Node, Node)
	 * 
	 * @param n
	 *            the node to unlink
	 */
	public void unlink(N n);

	/**
	 * Removes all edges {@code e} such that:
	 * <table>
	 * <tr>
	 * <th>{@code n1} is {@code null}</th>
	 * <th>{@code n2} is {@code null}</th>
	 * <th>Requirement for {@code e}</th>
	 * </tr>
	 * <tr>
	 * <td>no</td>
	 * <td>no</td>
	 * <td>{@link Edge#connects(Node, Node) e.connects(n1, n2)} is {@code true}</td>
	 * </tr>
	 * <tr>
	 * <td>no</td>
	 * <td>yes</td>
	 * <td>there exists some node {@code m} in the graph such that
	 * {@link Edge#connects(Node, Node) e.connects(n1, m)} returns {@code true}</td>
	 * </tr>
	 * <tr>
	 * <td>yes</td>
	 * <td>no</td>
	 * <td>there exists some node {@code m} in the graph such that
	 * {@link Edge#connects(Node, Node) e.connects(m, n2)} returns {@code true}</td>
	 * </tr>
	 * <tr>
	 * <td>yes</td>
	 * <td>yes</td>
	 * <td>{@code e} is any edge</td>
	 * </tr>
	 * </table>
	 * 
	 * This method therefore causes immediately subsequent calls to
	 * {@link #connected(Node, Node) connected(n1, n2)} return {@code false} as
	 * well.
	 * 
	 * @param n1
	 *            the first node to disconnect
	 * @param n2
	 *            the second node to disconnect
	 */
	public void unlink(N n1, N n2);

	/**
	 * Removes all edges from the graph.
	 */
	public void unlinkAll();

}
