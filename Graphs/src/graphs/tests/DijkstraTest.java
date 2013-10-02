package graphs.tests;

import static org.junit.Assert.*;
import graphs.algorithms.Dijkstra;
import graphs.core.AdjacencyGraph;
import graphs.core.BasicNode;
import graphs.core.DirectedWeightedEdge;
import graphs.core.MutableGraph;
import graphs.core.Node;
import graphs.core.WeightedEdgeGenerator;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class DijkstraTest {

	private MutableGraph<Integer, Node<Integer>, DirectedWeightedEdge<Integer, Double>> g;
	private WeightedEdgeGenerator<Integer, Double, DirectedWeightedEdge<Integer, Double>> gen;

	@Before
	public void setUp() throws Exception {
		gen = DirectedWeightedEdge.weightedGenerator();
		g = new AdjacencyGraph<Integer, Node<Integer>, DirectedWeightedEdge<Integer, Double>>(
				gen);
	}

	@Test
	public final void testFindShortestPathTree() {
		// setup graph as on p. 528 of CLR's "Introduction to Algorithms"
		Node<Integer> ns = new BasicNode<Integer>(0), nu = new BasicNode<Integer>(
				8), nv = new BasicNode<Integer>(13), nx = new BasicNode<Integer>(
				5), ny = new BasicNode<Integer>(7);

		g.add(ns);
		g.add(nu);
		g.add(nv);
		g.add(nx);
		g.add(ny);

		link(ns, nu, 10);
		link(ns, nx, 5);
		link(nu, nv, 1);
		link(nu, nx, 2);
		link(nx, nu, 3);
		link(nx, nv, 9);
		link(nx, ny, 2);
		link(ny, ns, 7);
		link(ny, nv, 6);
		link(nv, ny, 4);

		Dijkstra d = new Dijkstra();
		Map<Node<Integer>, Double> totalWeights = d.findShortestPathTree(g, ns);

		assertEquals("Size incorrect.", 5, totalWeights.size());
		
		Map<Node<Integer>, Double> expected = new HashMap<>();
		expected.put(ns, 0d);
		expected.put(nu, 8d);
		expected.put(nv, 9d);
		expected.put(nx, 5d);
		expected.put(ny, 7d);
		
		assertEquals("Contents incorrect.", expected, totalWeights);
	}

	private void link(Node<Integer> n1, Node<Integer> n2, double weight) {
		gen.nextWeight = weight;
		g.link(n1, n2);
	}
}
