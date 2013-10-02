package graphs.algorithms;

import graphs.core.DisjointSet;
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
	public <T, W extends Comparable<W>> List<WeightedEdge<T, W>> findMST(
			Graph<T, Node<T>, ? extends WeightedEdge<T, W>> graph) {
		// Create the final list of edges (the tree).
		List<WeightedEdge<T, W>> edges = new ArrayList<>();

		DisjointSet<Node<T>> forest = new DisjointSet<>();
		for (Node<T> node : graph.getNodes()) {
			forest.makeSet(node);
		}

		List<WeightedEdge<T, W>> allEdges = new ArrayList<>(graph.getEdges());
		Collections.sort(allEdges, new WeightedEdgeComparator<W>());
		for (WeightedEdge<T, W> edge : allEdges) {
			Node<T> u = edge.getHead(), v = edge.getTail();
			u = forest.findSet(u);
			v = forest.findSet(v);
			if (u != v) {
				edges.add(edge);
				forest.union(u, v);
			}
		}

		return edges;
	}

}
