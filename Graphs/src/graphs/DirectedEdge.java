package graphs;

/**
 * An edge in a directed graph. This edge's constructor takes two nodes,
 * {@code head} and {@code tail}. Any call to {@link #connects(Node, Node)} will
 * return {@code true} if and only if the given {@code n1 == head} and'
 * {@code n2 == tail}.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the type of value in the nodes connected by this edge
 */
public class DirectedEdge<T> implements Edge<T> {

	/**
	 * The head node of this edge.
	 */
	private final Node<? extends T> head;

	/**
	 * The tail node of this edge.
	 */
	private final Node<? extends T> tail;

	/**
	 * Creates an edge connecting the given head node to the given tail node.
	 * 
	 * @param head
	 *            the head node
	 * @param tail
	 *            the tail node
	 */
	public DirectedEdge(Node<? extends T> head, Node<? extends T> tail) {
		this.head = head;
		this.tail = tail;
	}

	@Override
	public final boolean connects(Node<T> n1, Node<T> n2) {
		return n1 == head && n2 == tail;
	}

	/**
	 * Creates a generator of directed edges.
	 * 
	 * @return a new generator
	 */
	public static final <T> EdgeGenerator<T, DirectedEdge<T>> generator () {
		class DirectedEdgeGenerator implements EdgeGenerator<T, DirectedEdge<T>> {
			@Override
			public DirectedEdge<T> createEdge(Node<T> n1, Node<T> n2) {
				return new DirectedEdge<T>(n1, n2);
			}
		}
		return new DirectedEdgeGenerator();
	}
	
}
