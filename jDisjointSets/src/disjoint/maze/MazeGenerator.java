package disjoint.maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import disjoint.DisjointSetForest;
import disjoint.DisjointSetForest.Node;

public class MazeGenerator {

	private class MazeNode {
		private MazeNode e, n, w, s;
		private boolean E = false, N = false, W = false, S = false;
	}

	private class MazeEdge {
		private DisjointSetForest<MazeNode>.Node a, b;
	}

	private Map<MazeNode, DisjointSetForest<MazeNode>.Node> lookup = new HashMap<>();
	private Grid<DisjointSetForest<MazeNode>.Node> nodeGrid;
	private DisjointSetForest<MazeNode> forest = new DisjointSetForest<>();
	private List<MazeEdge> edges = new ArrayList<>();

	public MazeGenerator(int width, int height) {
		nodeGrid = new Grid<>(height, width);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				final DisjointSetForest<MazeNode>.Node madeSet = forest
						.makeSet(new MazeNode());
				lookup.put(madeSet.getValue(), madeSet);
				nodeGrid.set(y, x, madeSet);
			}
		}
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				DisjointSetForest<MazeNode>.Node mnn = nodeGrid.at(y, x);
				MazeNode mn = mnn.getValue();
				if (x - 1 >= 0) {
					nodeGrid.at(y, x - 1).getValue().e = mn;
				}
				if (x + 1 < width) {
					nodeGrid.at(y, x + 1).getValue().w = mn;

					MazeEdge me = new MazeEdge();
					me.a = mnn;
					me.b = lookup.get(nodeGrid.at(y, x + 1));
					edges.add(me);
				}
				if (y - 1 >= 0) {
					nodeGrid.at(y - 1, x).getValue().n = mn;
				}
				if (y + 1 < height) {
					nodeGrid.at(y + 1, x).getValue().s = mn;

					MazeEdge me = new MazeEdge();
					me.a = mnn;
					me.b = lookup.get(nodeGrid.at(y + 1, x));
					edges.add(me);
				}
			}
		}
		Collections.shuffle(edges);
	}

	public void step() {
		if (!edges.isEmpty()) {
			MazeEdge e = edges.get(0);
			edges.remove(0);

			final MazeNode va = e.a.getValue();
			final MazeNode vb = e.b.getValue();
			if (!forest.isConnected(e.a, e.b)) {
				forest.union(e.a, e.b);
				if (va.e == vb) {
					va.E = true;
					vb.W = true;
				}
				if (va.n == vb) {
					va.N = true;
					vb.S = true;
				}
				if (va.w == vb) {
					va.W = true;
					vb.E = true;
				}
				if (va.s == vb) {
					va.S = true;
					vb.N = true;
				}
			}
		}
	}

	public void finish() {
		while (!edges.isEmpty()) {
			step();
		}
	}

	public static void main(String[] args) {
		MazeGenerator gen = new MazeGenerator(10, 8);
	}

}
