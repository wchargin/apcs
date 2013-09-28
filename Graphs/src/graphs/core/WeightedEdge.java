package graphs.core;

/**
 * An edge with an associated weight.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the type of value in the nodes connected by this edge
 * @param <W>
 *            the type of the codomain of the weight function represented by
 *            this edge
 */
public interface WeightedEdge<T, W extends Comparable<W>> extends Edge<T> {

	/**
	 * Gets the weight of this edge.
	 * 
	 * @return the weight
	 */
	public W getWeight();

}
