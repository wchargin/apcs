package graphs.core;

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
public class DirectedWeightedEdge<T, W extends Comparable<W>> extends
		DirectedEdge<T> implements WeightedEdge<T, W> {

	/**
	 * Creates a generator of directed edges.
	 * 
	 * @return a new generator
	 */
	public static final <T, W extends Comparable<W>> WeightedEdgeGenerator<T, W, DirectedWeightedEdge<T, W>> weightedGenerator() {
		class DirectedEdgeGenerator extends
				WeightedEdgeGenerator<T, W, DirectedWeightedEdge<T, W>> {
			@Override
			public DirectedWeightedEdge<T, W> createEdge(Node<T> n1, Node<T> n2) {
				return new DirectedWeightedEdge<T, W>(n1, n2, nextWeight);
			}
		}
		return new DirectedEdgeGenerator();
	}

	/**
	 * The weight of this edge.
	 */
	private final W weight;

	/**
	 * Creates an edge with the given weight connecting the given head node to
	 * the given tail node.
	 * 
	 * @param head
	 *            the head node
	 * @param tail
	 *            the tail node
	 * @param weight
	 *            the weight of the edge
	 */
	public DirectedWeightedEdge(Node<T> head, Node<T> tail, W weight) {
		super(head, tail);
		this.weight = weight;
	}

	@Override
	public W getWeight() {
		return weight;
	}

}
