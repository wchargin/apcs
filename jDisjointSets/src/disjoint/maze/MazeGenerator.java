package disjoint.maze;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

import disjoint.DisjointSetForest;
import disjoint.DisjointSetForest.Node;

public class MazeGenerator {

	private class MazeNode {
		private MazeNode e, n, w, s;
		private boolean E = false, N = false, W = false, S = false;

		@Override
		public String toString() {
			return "MazeNode [E=" + E + ", N=" + N + ", W=" + W + ", S=" + S
					+ "]";
		}

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

	public static void main(String[] args) throws IOException {
		final int height = 8;
		final int width = 10;
		MazeGenerator gen = new MazeGenerator(width, height);
		gen.finish();

		final int scale = 50;
		final int stroke = 5;
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

		final double gateWidth = 0.6d;
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
					g2d.fillRect(x + scale - stroke / 2, y + ioff, stroke,
							iwidth);
				}
				if (at.W) {
					g2d.fillRect(x - stroke / 2, y + ioff, stroke, iwidth);
				}
				if (at.N) {
					g2d.fillRect(x + ioff, y + scale - stroke / 2, iwidth,
							stroke);
				}
				if (at.S) {
					g2d.fillRect(x + ioff, y - stroke / 2, iwidth, stroke);
				}
			}
		}

		ImageIO.write(bg, "PNG", new File("H:\\maze.png"));
		JOptionPane.showMessageDialog(null, new ImageIcon(bg));
	}
}
