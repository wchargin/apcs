package disjoint.maze.jgamegui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import jgame.GObject;
import disjoint.maze.MazeGenerator;
import disjoint.maze.MazeGenerator.MazeNode;

public class MazePanel extends GObject {

	/**
	 * The maze generator used for this panel.
	 */
	private MazeGenerator gen;

	private int stroke = 1;

	public MazePanel() {
		super();
	}

	/**
	 * Completely finishes the maze generation.
	 */
	public void finish() {
		gen.finish();
	}

	public MazeGenerator getGenerator() {
		return gen;
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		if (gen == null) {
			return;
		}

		final BufferedImage tempImage = g.getDeviceConfiguration()
				.createCompatibleImage((int) Math.ceil(getWidth()),
						(int) Math.ceil(getHeight()), Transparency.TRANSLUCENT);
		Graphics2D gt = (Graphics2D) tempImage.getGraphics();
		gt.setStroke(new BasicStroke(stroke));

		final double panelWidth = getWidth();
		final double panelHeight = getHeight();
		final int mazeWidth = gen.getWidth();
		final int mazeHeight = gen.getHeight();

		final int mazeRow = (int) (panelHeight / mazeHeight);
		final int mazeCol = (int) (panelWidth / mazeWidth);
		gt.setComposite(AlphaComposite.Clear);
		gt.fillRect(0, 0, getIntWidth() + 1, getIntHeight() + 1);

		gt.setComposite(AlphaComposite.Src);
		{ // Draw mask

			gt.setColor(Color.BLACK);

			// Draw horizontals
			for (int y = 0; y < mazeHeight; y++) {
				gt.draw(new Line2D.Double(0, y * mazeRow, panelWidth, y
						* mazeRow));
			}

			// Draw verticals
			for (int x = 0; x < mazeWidth; x++) {
				gt.draw(new Line2D.Double(x * mazeCol, 0, x * mazeCol,
						panelHeight));
			}
		}
		gt.setComposite(AlphaComposite.Clear);
		{ // Remove gates
			gt.setColor(Color.WHITE);
			for (int i = 0; i < mazeWidth; i++) {
				for (int j = 0; j < mazeHeight; j++) {
					final MazeNode at = gen.nodeAt(j, i);
					int x = (int) (stroke / 2 + mazeCol * i);
					int y = (int) (stroke / 2 + mazeRow * j);
					if (at.getEast() != null) {
						gt.fillRect(x + mazeCol - stroke / 2, y + (int) stroke
								+ 1, stroke, mazeRow);
					}
					if (at.getWest() != null) {
						gt.fillRect(x - stroke / 2, y + (int) stroke + 1,
								stroke, mazeRow);
					}
					if (at.getNorth() != null) {
						gt.fillRect(x + (int) stroke + 1, y + mazeRow - stroke
								/ 2, mazeCol, stroke);
					}
					if (at.getSouth() != null) {
						gt.fillRect(x + (int) stroke + 1, y - stroke / 2,
								mazeCol, stroke);
					}

				}
			}

		}
		g.setColor(gen.isFinished() ? Color.GREEN : Color.WHITE);
		g.fillRect(0, 0, getIntWidth() + 1, getIntHeight() + 1);

		gt.setComposite(AlphaComposite.SrcIn);
		{ // Draw content
			gt.setColor(Color.BLACK);
			gt.fillRect(0, 0, getIntWidth() + 1, getIntHeight() + 1);
		}
		gt.dispose();
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getIntWidth() - 1, getIntHeight() - 1);
		g.drawImage(tempImage, 0, 0, null);
	}

	@Override
	public void preparePaint(Graphics2D g) {
		super.preparePaint(g);
		antialias(g);
	}

	public void setGenerator(MazeGenerator gen) {
		this.gen = gen;
	}

	/**
	 * Steps through one iteration of the maze generation.
	 */
	public void step() {
		while (!gen.step())
			;
	}

}
