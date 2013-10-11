package disjoint.maze;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import disjoint.DisjointSetForest;

public class MazeGenerator {

	private final int width;
	private final int height;

	/**
	 * Gets the width of the maze.
	 * 
	 * @return the width, in cells
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the height of the maze.
	 * 
	 * @return the height, in cells
	 */
	public int getHeight() {
		return height;
	}

	public class MazeNode {
		private MazeNode e, n, w, s;
		private boolean E = false, N = false, W = false, S = false;

		@Override
		public String toString() {
			return "MazeNode [E=" + E + ", N=" + N + ", W=" + W + ", S=" + S
					+ "]";
		}

		/**
		 * Gets the open node to the east, or {@code null} if there is no such
		 * node.
		 * 
		 * @return the node
		 */
		public MazeNode getEast() {
			return E ? e : null;
		}

		/**
		 * Gets the open node to the north, or {@code null} if there is no such
		 * node.
		 * 
		 * @return the node
		 */
		public MazeNode getNorth() {
			return N ? n : null;
		}

		/**
		 * Gets the open node to the west, or {@code null} if there is no such
		 * node.
		 * 
		 * @return the node
		 */
		public MazeNode getWest() {
			return W ? w : null;
		}

		/**
		 * Gets the open node to the south, or {@code null} if there is no such
		 * node.
		 * 
		 * @return the node
		 */
		public MazeNode getSouth() {
			return S ? s : null;
		}

	}

	public class MazeEdge {
		private DisjointSetForest<MazeNode>.Node a, b;
	}

	private Map<MazeNode, DisjointSetForest<MazeNode>.Node> lookup = new HashMap<>();
	private Grid<DisjointSetForest<MazeNode>.Node> nodeGrid;
	private DisjointSetForest<MazeNode> forest = new DisjointSetForest<>();
	private List<MazeEdge> edges = new ArrayList<>();

	public MazeGenerator(int width, int height) {
		nodeGrid = new Grid<>(this.height = height, this.width = width);
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
					me.b = lookup.get(nodeGrid.at(y, x + 1).getValue());
					edges.add(me);
				}
				if (y - 1 >= 0) {
					nodeGrid.at(y - 1, x).getValue().n = mn;
				}
				if (y + 1 < height) {
					nodeGrid.at(y + 1, x).getValue().s = mn;

					MazeEdge me = new MazeEdge();
					me.a = mnn;
					me.b = lookup.get(nodeGrid.at(y + 1, x).getValue());
					edges.add(me);
				}
			}
		}
		Collections.shuffle(edges);
	}

	public void step() {
		if (!isFinished()) {
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
		while (!isFinished()) {
			step();
		}
	}

	public boolean isFinished() {
		return edges.isEmpty();
	}

	/**
	 * Gets the maze node at the given row and column.
	 * 
	 * @param row
	 *            the row (y-coordinate)
	 * @param col
	 *            the column (x-coordinate)
	 * @return the relevant node
	 */
	public MazeNode nodeAt(int row, int col) {
		return nodeGrid.at(row, col).getValue();
	}

	public static void main(String[] args) throws IOException {
		final int height = 50;
		final int width = 50;
		MazeGenerator gen = new MazeGenerator(width, height);
		gen.finish();

		final int scale = 10;
		final int stroke = 2;
		BufferedImage bg = new BufferedImage(width * scale + stroke, height
				* scale + stroke, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) bg.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, bg.getWidth(), bg.getHeight());
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(stroke));

		for (int x = stroke / 2; x < bg.getWidth(); x += scale) {
			for (int y = stroke / 2; y < bg.getHeight(); y += scale) {
				g2d.drawRect(x, y, scale, scale);
			}
		}

		final double gateWidth = 1 - ((double) stroke / (double) scale);
		final double gateOffset = (1 - gateWidth) / 2;
		final int iwidth = (int) (gateWidth * scale);
		final int ioff = (int) (gateOffset * scale);
		g2d.setColor(Color.WHITE);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				final MazeNode at = gen.nodeGrid.at(j, i).getValue();
				int x = stroke / 2 + scale * i;
				int y = stroke / 2 + scale * j;
				if (at.E) {
					g2d.fillRect(x + scale - stroke / 2, y + ioff + 1, stroke,
							iwidth);
				}
				if (at.W) {
					g2d.fillRect(x - stroke / 2, y + ioff + 1, stroke, iwidth);
				}
				if (at.N) {
					g2d.fillRect(x + ioff + 1, y + scale - stroke / 2, iwidth,
							stroke);
				}
				if (at.S) {
					g2d.fillRect(x + ioff + 1, y - stroke / 2, iwidth, stroke);
				}
			}
		}

		JOptionPane.showMessageDialog(null, new ImageIcon(bg));
	}
}
