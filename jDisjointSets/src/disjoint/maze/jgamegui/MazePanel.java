package disjoint.maze.jgamegui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jgame.Context;
import jgame.GObject;
import jgame.controller.ControlScheme;
import jgame.controller.Controller;
import jgame.controller.EntranceExitController;
import jgame.controller.MovementTween;
import jgame.listener.DelayListener;
import disjoint.maze.MazeGenerator;
import disjoint.maze.MazeGenerator.MazeNode;
import disjoint.maze.jgamegui.MazeGUI.Views;

public class MazePanel extends GObject {

	public class Player extends GObject {

		private MazeNode currentNode;

		public Player() {
			class PlayerController implements Controller {
				private static final int DELAY = 4;
				private ControlScheme cs;

				public PlayerController(ControlScheme cs) {
					this.cs = cs;
				}

				@Override
				public void controlObject(GObject target, Context context) {
					if (currentNode == gen.getMaze().getEnd()) {
						context.setCurrentGameView(Views.WIN);
						target.removeSelf();
						player = null;
						return;
					}
					final Set<Integer> pressed = context.getKeyCodesPressed();
					boolean moved = false;
					double moveX = 0, moveY = 0;
					if (currentNode.hasEast() && pressed.contains(cs.rt)) {
						moved = true;
						currentNode = currentNode.getEast();
						moveX = calculateMazeCol();
					} else if (currentNode.hasWest() && pressed.contains(cs.lt)) {
						moved = true;
						currentNode = currentNode.getWest();
						moveX = -calculateMazeCol();
					} else if (currentNode.hasSouth()
							&& pressed.contains(cs.up)) {
						currentNode = currentNode.getSouth();
						moved = true;
						moveY = -calculateMazeRow();
					} else if (currentNode.hasNorth()
							&& pressed.contains(cs.dn)) {
						currentNode = currentNode.getNorth();
						moved = true;
						moveY = calculateMazeRow();
					}
					if (moved) {
						target.removeController(this);
						target.addListener(new DelayListener(DELAY) {
							@Override
							public void invoke(GObject target2, Context context) {
								target2.removeListener(this);
								target2.addController(PlayerController.this);
							}
						});
						target.addController(new MovementTween(DELAY, moveX,
								moveY));
					}
				}

			}
			addController(new PlayerController(ControlScheme.WASD));
		}

		@Override
		public void paint(Graphics2D g) {
			super.paint(g);
			g.setColor(Color.BLACK);
			g.fillOval(0, 0, getIntWidth() - 1, getIntHeight() - 1);
		}

		@Override
		public void preparePaint(Graphics2D g) {
			super.preparePaint(g);
			antialias(g);
		}

	}

	/**
	 * The maze generator used for this panel.
	 */
	private MazeGenerator gen;

	private int stroke = 1;

	/**
	 * The list of highlighted nodes.
	 */
	private List<MazeNode> highlighted = new ArrayList<>();

	private Player player;

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

	public List<MazeNode> getHighlighted() {
		return highlighted;
	}

	@Override
	public void paint(Graphics2D g) {
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

		final int mazeCol = calculateMazeCol();
		final int mazeRow = calculateMazeRow();
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
						gt.fillRect(x + mazeCol - stroke / 2, y + (int) stroke,
								stroke, mazeRow - 1);
					}
					if (at.getWest() != null) {
						gt.fillRect(x - stroke / 2, y + (int) stroke, stroke,
								mazeRow - 1);
					}
					if (at.getNorth() != null) {
						gt.fillRect(x + (int) stroke, y + mazeRow - stroke / 2,
								mazeCol - 1, stroke);
					}
					if (at.getSouth() != null) {
						gt.fillRect(x + (int) stroke, y - stroke / 2,
								mazeCol - 1, stroke);
					}

				}
			}

		}
		g.setColor(gen.isFinished() ? new Color(128, 255, 128) : Color.WHITE);
		g.fillRect(0, 0, getIntWidth() + 1, getIntHeight() + 1);

		gt.setComposite(AlphaComposite.SrcIn);
		{ // Draw content
			gt.setColor(Color.BLACK);
			gt.fillRect(0, 0, getIntWidth() + 1, getIntHeight() + 1);
		}

		antialias(gt);
		gt.setComposite(AlphaComposite.Src);
		{
			// Draw boxes
			gt.setColor(Color.BLUE);
			for (int i = 0; i < gen.getMaze().getWidth(); i++) {
				for (int j = 0; j < gen.getMaze().getHeight(); j++) {
					final MazeNode at = gen.nodeAt(j, i);
					int x = stroke / 2 + mazeCol * i;
					int y = stroke / 2 + mazeRow * j;
					if (highlighted.contains(at)) {
						gt.fillOval(x + mazeCol / 4, y + mazeRow / 4,
								mazeCol / 2, mazeRow / 2);
					}
				}
			}
		}
		gt.dispose();
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getIntWidth() - 1, getIntHeight() - 1);
		g.drawImage(tempImage, 0, 0, null);
		super.paint(g);
	}

	private int calculateMazeRow() {
		return (int) (getHeight() / gen.getHeight());
	}

	private int calculateMazeCol() {
		return (int) (getWidth() / gen.getWidth());
	}

	@Override
	public void preparePaint(Graphics2D g) {
		super.preparePaint(g);
		antialias(g);
	}

	public void setGenerator(MazeGenerator gen) {
		this.gen = gen;
		setHighlighted(new ArrayList<MazeNode>());
		player = null;
	}

	public void setHighlighted(List<MazeNode> highlighted) {
		this.highlighted = highlighted;
	}

	public void startPlay() {
		if (player != null) {
			return;
		}
		player = new Player();
		player.addController(EntranceExitController
				.createEntranceController(0.1));
		player.setAlpha(0);
		player.currentNode = gen.getMaze().getStart();
		final double mazeCol = getWidth() / gen.getWidth();
		final double mazeRow = getHeight() / gen.getHeight();
		player.setSize(mazeCol * 0.8, mazeRow * 0.8);
		addAt(player, mazeCol / 2, mazeRow / 2);
	}

	/**
	 * Steps through one iteration of the maze generation.
	 */
	public void step() {
		while (!gen.step())
			;
	}

}
