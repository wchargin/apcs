package graphs.algorithms;

import graphs.core.Edge;
import graphs.core.Graph;
import graphs.core.Node;
import graphs.core.WeightedEdge;
import graphs.core.WeightedEdgeComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An implementation of Kruskal's algorithm.
 * 
 * @author William Chargin
 * 
 */
public class Kruskal implements MSTAlgorithm {

	@Override
	public <T, W extends Comparable<W>> List<Edge<T>> findMST(
			Graph<T, Node<T>, WeightedEdge<T, W>> graph) {
		// Create the final list of edges (the tree).
		List<WeightedEdge<T, W>> edges = new ArrayList<>(); 
		
		List<WeightedEdge<T, W>> allEdges = new ArrayList<>(graph.getEdges());
		Collections.sort(allEdges, new WeightedEdgeComparator<W>());
		
		for (WeightedEdge<T, W> edge : allEdges) {
			
		}
		return null;
	}

}
