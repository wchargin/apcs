package graphs.core;

/**
 * An edge in an undirected graph. This edge's constructor takes two nodes,
 * {@code head} and {@code tail}. Any call to {@link #connects(Node, Node)} will
 * return {@code true} if and only if the given {@code n1 == head} and'
 * {@code n2 == tail} or vice versa.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the type of value in the nodes connected by this edge
 */
public class UndirectedEdge<T> implements Edge<T> {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((head == null) ? 0 : head.hashCode());
		result = prime * result + ((tail == null) ? 0 : tail.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UndirectedEdge other = (UndirectedEdge) obj;
		if (head == null) {
			if (other.head != null)
				return false;
		} else if (!head.equals(other.head))
			return false;
		if (tail == null) {
			if (other.tail != null)
				return false;
		} else if (!tail.equals(other.tail))
			return false;
		return true;
	}

	/**
	 * Creates a generator of undirected edges.
	 * 
	 * @return a new generator
	 */
	public static final <T> EdgeGenerator<T, UndirectedEdge<T>> generator() {
		class UndirectedEdgeGenerator implements
				EdgeGenerator<T, UndirectedEdge<T>> {
			@Override
			public UndirectedEdge<T> createEdge(Node<T> n1, Node<T> n2) {
				return new UndirectedEdge<T>(n1, n2);
			}
		}
		return new UndirectedEdgeGenerator();
	}

	/**
	 * The head node of this edge.
	 */
	private final Node<T> head;

	/**
	 * The tail node of this edge.
	 */
	private final Node<T> tail;

	/**
	 * Creates an edge connecting the given head node to the given tail node.
	 * 
	 * @param head
	 *            the head node
	 * @param tail
	 *            the tail node
	 */
	public UndirectedEdge(Node<T> head, Node<T> tail) {
		this.head = head;
		this.tail = tail;
	}

	@Override
	public final boolean connects(Node<T> n1, Node<T> n2) {
		return (n1 == head && n2 == tail) || (n1 == tail && n2 == head);
	}

	@Override
	public Node<T> getHead() {
		return head;
	}

	@Override
	public Node<T> getTail() {
		return tail;
	}
}
