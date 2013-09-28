package graphs.tests;

import static org.junit.Assert.fail;
import graphs.core.AdjacencyGraph;
import graphs.core.EdgeGenerator;
import graphs.core.Node;
import graphs.core.WeightedEdge;

import org.junit.Before;
import org.junit.Test;

public class KruskalTest {
	
	private AdjacencyGraph<Integer, Node<Integer>, WeightedEdge<Integer, Double>> graph;
	private EdgeGenerator<Integer, WeightedEdge<Integer, Double>> gen;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testKruskal() {
		fail("Not yet implemented");
	}

}
