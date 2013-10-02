package graphs.algorithms;

import graphs.core.Graph;
import graphs.core.Node;
import graphs.core.WeightedEdge;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Implementation of Dijkstra's algorithm for finding the single-source shortest
 * path tree.
 * 
 * @author William Chargin
 * 
 */
public class Dijkstra {

	/**
	 * Finds the single-source shortest path tree for the given graph and
	 * source.
	 * 
	 * @param graph
	 *            the graph
	 * @param source
	 *            the source node
	 * @return
	 */
	public <T, W extends Number & Comparable<W>> Map<Node<T>, Double> findShortestPathTree(
			Graph<T, Node<T>, ? extends WeightedEdge<T, W>> graph,
			Node<T> source) {
		final Map<Node<T>, Double> distances = new HashMap<>();

		PriorityQueue<Node<T>> queue = new PriorityQueue<Node<T>>(graph
				.getNodes().size(), new Comparator<Node<T>>() {
			@Override
			public int compare(Node<T> o1, Node<T> o2) {
				return distances.get(o1).compareTo(distances.get(o2));
			}
		});

		for (Node<T> node : graph.getNodes()) {
			distances.put(node, node == source ? 0 : Double.MAX_VALUE);
			queue.add(node);
		}
		while (!queue.isEmpty()) {
			Node<T> closest = null;
			double distance = Double.MAX_VALUE;
			for (Node<T> node : queue) {
				Double thisDistance = distances.get(node);
				if (thisDistance < distance) {
					closest = node;
					distance = thisDistance;
				}
			}

			queue.remove(closest);
			if (distance == Double.MAX_VALUE) {
				break;
			}

			for (WeightedEdge<T, W> edge : graph.getNeighboringEdges(closest)) {
				double totalDistance = distance
						+ edge.getWeight().doubleValue();
				Node<T> tail = edge.getTail();
				if (totalDistance < distances.get(tail)) {
					distances.put(tail, totalDistance);
					// remove and re-add to priority queue to force re-heapify
					queue.remove(tail);
					queue.add(tail);
				}
			}
		}
		return distances;
	}
}
