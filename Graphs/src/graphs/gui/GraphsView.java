package graphs.gui;

import graphs.core.AdjacencyGraph;
import graphs.core.BasicNode;
import graphs.core.Edge;
import graphs.core.EdgeGenerator;
import graphs.core.MutableGraph;
import graphs.core.Node;
import graphs.core.UndirectedEdge;
import graphs.gui.GraphsGUI.GraphSettings;
import graphs.gui.GraphsGUI.Views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import jgame.Context;
import jgame.GButton;
import jgame.GContainer;
import jgame.GObject;
import jgame.listener.ButtonListener;

public class GraphsView extends GContainer {

	public class EdgeView extends GObject {

		@Override
		public void paint(Graphics2D g) {
			super.paint(g);

			g.setColor(Color.BLACK);
			for (NodeView view : nodes) {
				for (NodeView other : nodes) {
					Node<Integer> otherNode = other.getNode();
					Node<Integer> node = view.getNode();
					if (graph.connected(node, otherNode)) {
						Line2D l2d = new Line2D.Double(view.getX(),
								view.getY(), other.getX(), other.getY());
						g.draw(l2d);
					}
				}
			}
		}

		@Override
		public void preparePaint(Graphics2D g) {
			super.preparePaint(g);
			GObject.antialias(g);
		}

	}

	private GraphSettings settings;

	private List<NodeView> nodes = new ArrayList<>();
	private MutableGraph<Integer, Node<Integer>, Edge<Integer>> graph;

	private NodeView linkBegin;

	{
		EdgeGenerator<Integer, ? extends Edge<Integer>> gen = UndirectedEdge
				.generator();
		graph = new AdjacencyGraph<>(gen);
	}

	private EdgeView edges = new EdgeView();

	public GraphsView(GraphSettings gs) {
		setSize(800, 600);
		setAnchorTopLeft();
		settings = gs;

		add(edges);
		edges.setSize(800, 600);
		edges.setAnchorTopLeft();

		GButton btnAddNode = createButton("Add Node");
		addButton(btnAddNode, 0);
		btnAddNode.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				addNode();
			}
		});

		GButton btnClear = createButton("Clear");
		addButton(btnClear, 1);
		btnClear.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				clear();
			}
		});

		GButton btnDijkstra = createButton("Dijkstra");
		addButton(btnDijkstra, 2);

		GButton btnKruskal = createButton("Kruskal");
		addButton(btnKruskal, 3);

		GButton btnSettings = createButton("Settings");
		addButton(btnSettings, 5);
		btnSettings.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				context.setCurrentGameView(Views.SETTINGS);
			}
		});

		GButton btnHelp = createButton("Help");
		addButton(btnHelp, 6);
		btnHelp.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				context.setCurrentGameView(Views.HELP);
			}
		});

	}

	protected void addNode() {
		final NodeView n = new NodeView(settings, new BasicNode<Integer>(
				nodes.size() + 1));
		nodes.add(n);
		graph.add(n.getNode());
		addAt(n, Math.random() * 700 + 50, Math.random() * 400 + 50);
	}

	protected void clear() {
		for (NodeView n : nodes) {
			remove(n);
		}
		nodes.clear();
	}

	private GButton createButton(String text) {
		return ComponentGenerator.attachLabel(
				ComponentGenerator.createButton(), text);
	}

	private void addButton(GObject button, int buttonIndex) {
		addAt(button, buttonIndex * 110 + 60, 560);
	}

	public void link(NodeView nodeView) {
		if (linkBegin != null) {
			graph.link(linkBegin.getNode(), nodeView.getNode());
			nodeView.setLinking(false);
			linkBegin.setLinking(false);
			linkBegin = null;
		} else {
			linkBegin = nodeView;
		}
	}

	public void cancelLink(NodeView nodeView) {
		linkBegin = null;
	}

}
