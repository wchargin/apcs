package disjoint.maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import disjoint.DisjointSetForest;

public class SimpleMazeGenerator {

	private static final int N = 1, S = 2, E = 4, W = 8;
	private static final Map<Integer, Integer> DX, DY, OPPOSITE;
	static {
		HashMap<Integer, Integer> dx = new HashMap<>(), dy = new HashMap<>(), opposite = new HashMap<>();
		dx.put(N, 0);
		dx.put(S, 0);
		dx.put(E, 1);
		dx.put(W, -1);
		dy.put(N, 1);
		dy.put(S, -1);
		dy.put(E, 0);
		dy.put(W, 0);
		opposite.put(N, S);
		opposite.put(S, N);
		opposite.put(E, W);
		opposite.put(W, E);
		DX = Collections.unmodifiableMap(dx);
		DY = Collections.unmodifiableMap(dy);
		OPPOSITE = Collections.unmodifiableMap(opposite);
	}

	private ArrayList<SimpleEdge> edges = new ArrayList<>();

	private class SimpleEdge {
		public int x;
		public int y;
		public int direction;

		public SimpleEdge(int x, int y, int direction) {
			super();
			this.x = x;
			this.y = y;
			this.direction = direction;
		}
	}

	private static final int WIDTH = 10, HEIGHT = 10;
	private Grid<Integer> grid = new Grid<>(WIDTH, HEIGHT);
	private Grid<DisjointSetForest<Object>.Node> sets = new Grid<>(WIDTH,
			HEIGHT);
	private DisjointSetForest<Object> forest = new DisjointSetForest<>();

	public void main() {
		// init edges
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				if (y > 0) {
					edges.add(new SimpleEdge(x, y, N));
				}
				if (x > 0) {
					edges.add(new SimpleEdge(x, y, W));
				}
				sets.set(x, y, forest.makeSet(new Object()));
				grid.set(x, y, 0);
			}
		}
		Collections.shuffle(edges);

		while (!edges.isEmpty()) {
			SimpleEdge e = edges.get(0);
			edges.remove(0);

			int x = e.x, y = e.y;

			int x2 = e.x + DX.get(e.direction);
			int y2 = e.y + DY.get(e.direction);

			if (y2 >= HEIGHT || x2 >= WIDTH) {
				continue;
			}

			DisjointSetForest<Object>.Node node1 = sets.at(y, x), node2 = sets
					.at(y2, x2);

			if (!forest.isConnected(node1, node2)) {
				forest.union(node1, node2);
				grid.set(y, x, grid.at(y, x) | e.direction);
				grid.set(y2, x2, grid.at(y2, x2) | OPPOSITE.get(e.direction));
			}
		}

		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				int cell = grid.at(y, x);
				System.out.print((cell & S) != 0 ? " " : "_");
				if ((cell & E) != 0) {
					System.out
							.print((((cell & grid.at(y, x + 1)) & S) != 0) ? " "
									: "_");
				} else {
					System.out.print("|");
				}
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		new SimpleMazeGenerator().main();
	}
}
