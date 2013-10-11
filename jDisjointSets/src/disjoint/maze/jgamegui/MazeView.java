package disjoint.maze.jgamegui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import jgame.Context;
import jgame.GContainer;
import jgame.GObject;
import jgame.listener.TimerListener;
import disjoint.maze.MazeGenerator;
import disjoint.maze.MazeGenerator.MazeNode;

public class MazeView extends GContainer {

	private MazeGenerator gen;

	public MazeView() {
		setBackgroundColor(Color.WHITE);
		setSize(401, 401);
		gen = new MazeGenerator(20, 20);
		addListener(new TimerListener(2) {
			@Override
			public void invoke(GObject target, Context context) {
				gen.step();
				gen.step();
				gen.step();
				gen.step();
				if (gen.isFinished()) {
					target.removeListener(this);
					setBackgroundColor(Color.YELLOW);
				}
			}
		});
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		final int height = 20;
		final int width = 20;

		final int scale = getIntWidth() / width;
		final int stroke = 2;
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(stroke));

		for (int x = stroke / 2; x < getWidth(); x += scale) {
			for (int y = stroke / 2; y < getHeight(); y += scale) {
				g.drawRect(x, y, scale, scale);
			}
		}

		final double gateWidth = 1 - ((double) stroke / (double) scale);
		final double gateOffset = (1 - gateWidth) / 2;
		final int iwidth = (int) (gateWidth * scale);
		final int ioff = (int) (gateOffset * scale);
		g.setColor(getBackgroundColor());
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				final MazeNode at = gen.nodeAt(j, i);
				int x = stroke / 2 + scale * i;
				int y = stroke / 2 + scale * j;
				if (at.getEast() != null) {
					g.fillRect(x + scale - stroke / 2, y + ioff + 1, stroke,
							iwidth);
				}
				if (at.getWest() != null) {
					g.fillRect(x - stroke / 2, y + ioff + 1, stroke, iwidth);
				}
				if (at.getNorth() != null) {
					g.fillRect(x + ioff + 1, y + scale - stroke / 2, iwidth,
							stroke);
				}
				if (at.getNorth() != null) {
					g.fillRect(x + ioff + 1, y - stroke / 2, iwidth, stroke);
				}
			}
		}
	}

}
