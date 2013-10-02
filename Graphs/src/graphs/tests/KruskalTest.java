package graphs.tests;

import static org.junit.Assert.assertEquals;
import graphs.algorithms.Kruskal;
import graphs.core.AdjacencyGraph;
import graphs.core.BasicNode;
import graphs.core.MutableGraph;
import graphs.core.Node;
import graphs.core.UndirectedWeightedEdge;
import graphs.core.WeightedEdge;
import graphs.core.WeightedEdgeGenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class KruskalTest {

	private MutableGraph<Integer, Node<Integer>, UndirectedWeightedEdge<Integer, Double>> g;
	private WeightedEdgeGenerator<Integer, Double, UndirectedWeightedEdge<Integer, Double>> gen;

	@Before
	public void setUp() throws Exception {
		gen = UndirectedWeightedEdge.weightedGenerator();
		g = new AdjacencyGraph<Integer, Node<Integer>, UndirectedWeightedEdge<Integer, Double>>(
				gen);
	}

	@Test
	public final void testFindMST() {
		// setup graph as on p. 506 of CLR's "Introduction to Algorithms"
		Node<Integer> na = new BasicNode<Integer>(1), nb = new BasicNode<Integer>(
				2), nc = new BasicNode<Integer>(3), nd = new BasicNode<Integer>(
				4), ne = new BasicNode<Integer>(5), nf = new BasicNode<Integer>(
				6), ng = new BasicNode<Integer>(7), nh = new BasicNode<Integer>(
				8), ni = new BasicNode<Integer>(9);

		g.add(na);
		g.add(nb);
		g.add(nc);
		g.add(nd);
		g.add(ne);
		g.add(nf);
		g.add(ng);
		g.add(nh);
		g.add(ni);

		link(na, nb, 4);
		link(na, nh, 8);
		link(nb, nc, 8);
		link(nb, nh, 11);
		link(nh, ni, 7);
		link(nh, ng, 1);
		link(ni, nc, 2);
		link(ni, ng, 6);
		link(nc, nf, 4);
		link(nc, nd, 9);
		link(ng, nf, 2);
		link(nd, ne, 9);
		link(nd, nf, 14);
		link(ne, nf, 10);
		
		Kruskal k = new Kruskal();
		List<WeightedEdge<Integer, Double>> mst = k.findMST(g);

		System.out.println(mst);
		assertEquals("Size incorrect.", 8, mst.size());
		Set<UndirectedWeightedEdge<Integer, Double>> l = new HashSet<>();
		l.add(new UndirectedWeightedEdge<Integer, Double>(na, nb, 4d));
		l.add(new UndirectedWeightedEdge<Integer, Double>(na, nh, 8d));
		l.add(new UndirectedWeightedEdge<Integer, Double>(nh, ng, 1d));
		l.add(new UndirectedWeightedEdge<Integer, Double>(ng, nf, 2d));
		l.add(new UndirectedWeightedEdge<Integer, Double>(nf, nc, 4d));
		l.add(new UndirectedWeightedEdge<Integer, Double>(nc, ni, 2d));
		l.add(new UndirectedWeightedEdge<Integer, Double>(nc, nd, 7d));
		l.add(new UndirectedWeightedEdge<Integer, Double>(nd, ne, 9d));
		
		assertEquals(l, new HashSet<>(mst));
	}

	private void link(Node<Integer> n1, Node<Integer> n2, double weight) {
		gen.nextWeight = weight;
		g.link(n1, n2);
		g.link(n2, n1);
	}
}
