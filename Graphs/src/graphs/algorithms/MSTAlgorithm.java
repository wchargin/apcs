package graphs.algorithms;

import graphs.core.Edge;
import graphs.core.Graph;
import graphs.core.Node;
import graphs.core.Operation;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic minimum spanning tree algorithm, as described on page 500 of
 * <em>Introduction to Algorithms</em> (CLR).
 * 
 * @author William Chargin
 * 
 */
public abstract class MSTAlgorithm {

	/**
	 * Finds a safe edge for the current subset of a minimum spanning tree
	 * {@code root} and the given graph {@code graph} for which the minimum
	 * spanning tree will eventually be formed.
	 * 
	 * @param root
	 *            the current, growing, tree
	 * @param graph
	 *            the graph from which the tree is constructed
	 * @return a safe edge that maintains the minimum spanning tree property
	 */
	protected abstract <T> Edge<T> findSafeEdge(List<Edge<T>> current,
			Graph<T, Node<T>, Edge<T>> graph);

	public <T> List<Edge<T>> findMST(Graph<T, Node<T>, Edge<T>> graph,
			Node<T> source) {
		final List<Edge<T>> mst = new ArrayList<Edge<T>>();

		// use a final array instead of a boolean so that we can access it from
		// within an anonymous class (Java doesn't capture properly)
		final boolean[] isMST = { false };
		Operation<Edge<T>> testMST = new Operation<Edge<T>>() {

			@Override
			public void invoke(Edge<T> t) {
				if (!isMST[0]) {
					return;
				}
				if (!mst.contains(t)) {
					isMST[0] = false;
				}
			}

		};
		do {
			Edge<T> safeEdge = findSafeEdge(mst, graph);
			mst.add(safeEdge);

			// determine if tree is a spanning tree
			isMST[0] = true;
			graph.foreachEdge(testMST);
		} while (!isMST[0]);
		throw new UnsupportedOperationException();
	}
}
