package graphs.core;

import java.util.Comparator;

/**
 * A comparator for weighted edges.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the type of value in the nodes connected by this edge
 * @param <W>
 *            the type of the codomain of the weight function represented by
 *            this edge
 */
public class WeightedEdgeComparator<W extends Comparable<W>> implements
		Comparator<WeightedEdge<?, W>> {

	@Override
	public int compare(WeightedEdge<?, W> o1, WeightedEdge<?, W> o2) {
		return o1.getWeight().compareTo(o1.getWeight());
	}

}
