package graphs.gui;

import graphs.core.Node;
import graphs.gui.GraphsGUI.GraphSettings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;

import jgame.Context;
import jgame.GMessage;
import jgame.GObject;
import jgame.controller.MouseLocationController;
import jgame.controller.PulsateController;
import jgame.listener.ButtonListener;
import jgame.listener.FrameListener;
import jgame.listener.LocalKeyListener;

public class NodeView extends GObject {

	private final GraphSettings settings;
	private Node<Integer> node;

	private GMessage message = new GMessage();
	private RectangularShape bounds = new Ellipse2D.Double();

	public NodeView(GraphSettings gs, Node<Integer> node) {
		setSize(100, 50);
		settings = gs;
		setNode(node);

		class ShapeComponent extends GObject {

			private final Shape shape;
			private final Paint stroke, fill;

			public ShapeComponent(Shape shape, Paint stroke, Paint fill) {
				super();
				this.shape = shape;
				this.stroke = stroke;
				this.fill = fill;
			}

			@Override
			public void paint(Graphics2D g) {
				super.paint(g);
				g.setPaint(fill);
				g.fill(shape);
				g.setPaint(stroke);
				g.draw(shape);
			}

			@Override
			public void preparePaint(Graphics2D g) {
				super.preparePaint(g);
				setSize(NodeView.this.getWidth(), NodeView.this.getHeight());
				bounds.setFrame(0, 0, getWidth() - 2, getHeight() - 2);
				GObject.antialias(g);
			}

		}

		final ShapeComponent sc = new ShapeComponent(bounds, Color.BLACK,
				Color.WHITE);
		sc.setAnchorTopLeft();
		add(sc);

		add(message);
		message.setAnchorTopLeft();
		message.setAlignmentX(0.5);
		message.setAlignmentY(0.5);

		addListener(new FrameListener() {
			@Override
			public void invoke(GObject target, Context context) {
				message.setFontSize(settings.textSize);
				message.setSize(getWidth(), getHeight());
			}
		});

		final MouseLocationController mouse = new MouseLocationController();
		final PulsateController dragging = new PulsateController(0.95, 0.05, 15);
		addListener(new ButtonListener() {
			{
				setValidButtonMask(MouseEvent.BUTTON2_MASK);
			}

			@Override
			public void mouseClicked(Context context) {
				removeController(mouse);
				removeController(dragging);
				setScale(1);
			}

			@Override
			public void mouseDown(Context context) {
				addController(mouse);
				addController(dragging);
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
	}

	public Node<?> getNode() {
		return node;
	}

	public void setNode(Node<Integer> node) {
		this.node = node;
		message.setText(this.node == null ? "" : this.node.getValue()
				.toString());
	}

}
