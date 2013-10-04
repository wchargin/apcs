package graphs.gui;

import graphs.core.Node;
import graphs.gui.GraphsGUI.GraphSettings;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;

import jgame.Context;
import jgame.GMessage;
import jgame.GObject;
import jgame.controller.MouseLocationController;
import jgame.listener.ButtonListener;
import jgame.listener.GlobalKeyListener;
import jgame.listener.LocalKeyListener;

public class NodeView extends GObject {

	private final GraphSettings settings;
	private Node<Integer> node;

	private GMessage message = new GMessage();
	private RectangularShape bounds = new Ellipse2D.Double();

	private boolean pressed = false;
	private boolean linking = false;

	public NodeView(GraphSettings gs, Node<Integer> node) {
		setSize(100, 50);
		settings = gs;
		setNode(node);

		class ShapeComponent extends GObject {

			private Shape shape;

			public ShapeComponent(Shape shape) {
				super();
				this.shape = shape;
			}

			@Override
			public void paint(Graphics2D g) {
				super.paint(g);
				g.setPaint(pressed ? new Color(0.9f, 0.9f, 0.9f) : Color.WHITE);
				g.fill(shape);
				g.setColor(Color.BLACK);
				g.setStroke(new BasicStroke(getBorderWidth()));
				g.draw(shape);
			}

			@Override
			public void preparePaint(Graphics2D g) {
				super.preparePaint(g);
				setSize(NodeView.this.getWidth(), NodeView.this.getHeight());
				bounds.setFrame(getBorderWidth(), getBorderWidth(), getWidth()
						- 1 - getBorderWidth(), getHeight() - 1
						- getBorderWidth());
				GObject.antialias(g);
			}

			private float getBorderWidth() {
				return linking ? 2 : 1;
			}

		}

		final ShapeComponent sc = new ShapeComponent(bounds);
		sc.setAnchorTopLeft();
		add(sc);

		add(message);
		message.setAnchorTopLeft();
		message.setAlignmentX(0.5);
		message.setAlignmentY(0.5);

		final MouseLocationController mouse = new MouseLocationController();
		addListener(new ButtonListener() {
			{
				setValidButtonMask(MouseEvent.BUTTON3_MASK);
			}

			@Override
			public void mouseClicked(Context context) {
				removeController(mouse);
				pressed = false;
				setScale(1);
			}

			@Override
			public void mouseDown(Context context) {
				addController(mouse);
				pressed = true;
			}
		});

		addListener(new LocalKeyListener(KeyEvent.VK_I) {
			@Override
			public void invoke(GObject target, Context context) {
				setHeight(Math.max(20, getHeight() + 5));
			}
		});
		addListener(new LocalKeyListener(KeyEvent.VK_K) {
			@Override
			public void invoke(GObject target, Context context) {
				setHeight(Math.max(20, getHeight() - 5));
			}
		});
		addListener(new LocalKeyListener(KeyEvent.VK_L) {
			@Override
			public void invoke(GObject target, Context context) {
				setWidth(Math.max(20, getWidth() + 5));
			}
		});
		addListener(new LocalKeyListener(KeyEvent.VK_J) {
			@Override
			public void invoke(GObject target, Context context) {
				setWidth(Math.max(20, getWidth() - 5));
			}
		});
		addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				linking = true;
				getFirstAncestorOf(GraphsView.class)
						.link(NodeView.this);
			}
		});
		addListener(new GlobalKeyListener(KeyEvent.VK_ESCAPE) {
			@Override
			public void invoke(GObject target, Context context) {
				linking = false;
				getFirstAncestorOf(GraphsView.class).cancelLink(NodeView.this);
			}
		});
	}

	public void setLinking(boolean linking) {
		this.linking = linking;
	}

	@Override
	public Shape getBoundingShape() {
		return new Ellipse2D.Double(-1, -1, getWidth() + 2, getHeight() + 2);
	}

	@Override
	public void preparePaint(Graphics2D g) {
		super.preparePaint(g);
		message.setFontSize(settings.textSize);
		message.setSize(getWidth(), getHeight());
	}

	public Node<Integer> getNode() {
		return node;
	}

	public void setNode(Node<Integer> node) {
		this.node = node;
		message.setText(this.node == null ? "" : this.node.getValue()
				.toString());
	}

}
