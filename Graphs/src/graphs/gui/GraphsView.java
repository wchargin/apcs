package graphs.gui;

import graphs.algorithms.Kruskal;
import graphs.core.AdjacencyGraph;
import graphs.core.BasicNode;
import graphs.core.DirectedEdge;
import graphs.core.Edge;
import graphs.core.MutableGraph;
import graphs.core.Node;
import graphs.core.UndirectedWeightedEdge;
import graphs.core.WeightedEdge;
import graphs.core.WeightedEdgeGenerator;
import graphs.gui.GraphsGUI.GraphSettings;
import graphs.gui.GraphsGUI.Views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jgame.Context;
import jgame.GButton;
import jgame.GContainer;
import jgame.GMessage;
import jgame.GObject;
import jgame.controller.AlphaTween;
import jgame.controller.ScaleTween;
import jgame.listener.ButtonListener;
import jgame.listener.FrameListener;
import jgame.listener.Listener;

public class GraphsView extends GContainer {

	@Override
	public void viewShown() {
		if (settings.useDistanceWeight) {
			// temporary map
			Map<Node<String>, NodeView> map = new HashMap<>();
			for (NodeView nv : nodes) {
				map.put(nv.getNode(), nv);
			}
			Set<? extends WeightedEdge<String, Double>> edges = graph
					.getEdges();
			for (WeightedEdge<String, Double> edge : edges) {
				edge.setWeight(calculateWeightByDistance(
						map.get(edge.getHead()), map.get(edge.getTail())));
			}
		}
	}

	public class EdgeView extends GObject {

		private class WeightBubble extends GMessage {
			public WeightBubble() {
				setSize(30, 30);
				setAlignmentX(0.5);
				setAlignmentY(0.5);
				setColor(Color.YELLOW);
				setFontStyle(Font.BOLD);
			}

			@Override
			public void paint(Graphics2D g) {
				g.setColor(Color.BLACK);
				g.fillOval(0, 0, getIntWidth(), getIntHeight());
				super.paint(g);
			}

			public void setWeight(double weight) {
				setText(Double.toString(weight));
			}
		}

		private WeightedEdge<String, Double> dirtyEdge;
		private Point2D initialPoint;
		private double initialWeight;

		private Collection<? extends Edge<String>> mst;

		private WeightBubble weightMessage;

		{
			addListener(new FrameListener() {
				@Override
				public void invoke(GObject target, Context context) {
					if (settings.useDistanceWeight) {
						return;
					}
					if ((context.getMouseButtonMask() & MouseEvent.BUTTON1_MASK) != 0) {
						if (dirtyEdge == null) {
							Point2D mouse = context.getMouseRelative();

							// find edge within 5px
							Set<? extends WeightedEdge<String, Double>> graphEdges = graph
									.getEdges();
							Line2D line = null;
							outer: for (NodeView view : nodes) {
								if (view.getAbsoluteBoundingShape().contains(
										context.getMouseAbsolute())) {
									continue;
								}
								Node<String> node = view.getNode();
								for (NodeView other : nodes) {
									Node<String> otherNode = other.getNode();
									if (graph.connected(node, otherNode)) {
										Line2D l2d = new Line2D.Double(
												view.getX(), view.getY(),
												other.getX(), other.getY());
										if (l2d.ptSegDist(mouse) < 5) {
											// this is the edge
											// find it in the list of edges
											for (WeightedEdge<String, Double> edge : graphEdges) {
												if (edge.getHead() == node
														&& edge.getTail() == otherNode) {
													line = l2d;
													dirtyEdge = edge;
													break outer;
												}
											}
										}
									}
								}
							} // end outer
							if (dirtyEdge != null) {
								mst = null;
								initialPoint = mouse;
								initialWeight = dirtyEdge.getWeight();
								weightMessage = new WeightBubble();
								weightMessage.setWeight(dirtyEdge.getWeight());
								addAt(weightMessage,
										(line.getX1() + line.getX2()) / 2,
										(line.getY1() + line.getY2()) / 2);
							}
						} // end if dirtyEdge was null at start of method
						else {
							double mouseDisplacement = context
									.getMouseRelative().getX()
									- initialPoint.getX();
							int weightDisplacement = (int) (mouseDisplacement / 5);
							dirtyEdge.setWeight(initialWeight
									+ weightDisplacement);
							weightMessage.setWeight(dirtyEdge.getWeight());
						}
					} else {
						if (dirtyEdge != null) {
							weightMessage.removeSelf();
							dirtyEdge = null;
							weightMessage = null;
							initialPoint = null;
							initialWeight = 0;
						}
					}
				}
			});
		}

		@Override
		public void paint(Graphics2D g) {

			g.setColor(Color.BLACK);
			for (NodeView view : nodes) {
				Node<String> node = view.getNode();
				for (NodeView other : nodes) {
					Node<String> otherNode = other.getNode();
					if (graph.connected(node, otherNode)) {
						Line2D l2d = new Line2D.Double(view.getX(),
								view.getY(), other.getX(), other.getY());
						// stroke width asymptotically approaches 10
						// 10 (1 - r^(w/k))
						// w = weight
						// r = convergence time
						// k = scalar for weight

						// find edge
						Set<? extends WeightedEdge<String, Double>> neighboringEdges = graph
								.getNeighboringEdges(node);
						for (WeightedEdge<String, Double> edge : neighboringEdges) {
							if (edge.getTail() == otherNode) {
								Color headColor = mst != null
										&& mst.contains(edge) ? Color.RED
										: Color.BLACK;

								Color tailColor = (edge instanceof DirectedEdge) ? new Color(
										headColor.getRGB() & 0x1FFFFFFF, true)
										: headColor;
								g.setPaint(new GradientPaint(
										new Point2D.Double(view.getX(), view
												.getY()), headColor,
										new Point2D.Double(other.getX(), other
												.getY()), tailColor));

								float width = (float) (10 * (1f - Math
										.pow(0.8f,
												Math.abs(edge.getWeight())
														/ (settings.useDistanceWeight ? 6f
																: 3f))));
								if (edge.getWeight() >= 0) {
									g.setStroke(new BasicStroke(width));
								} else {
									g.setStroke(new BasicStroke(width,
											BasicStroke.CAP_BUTT,
											BasicStroke.JOIN_ROUND, 10.0f,
											new float[] { width * 4 }, 0));
								}
							}
						}
						g.draw(l2d);
					}
				}
				super.paint(g);
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
	private MutableGraph<String, Node<String>, ? extends WeightedEdge<String, Double>> graph;

	private NodeView linkBegin;
	private WeightedEdgeGenerator<String, Double, ? extends Edge<String>> gen = UndirectedWeightedEdge
			.weightedGenerator();

	{
		gen.nextWeight = 1d;
		graph = new AdjacencyGraph<String, Node<String>, WeightedEdge<String, Double>>(
				gen);
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

		GButton btnWipeNodes = createButton("Wipe Nodes");
		addButton(btnWipeNodes, 1);
		btnWipeNodes.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				wipeNodes();
			}
		});

		GButton btnWipeEdges = createButton("Wipe Edges");
		addButton(btnWipeEdges, 2);
		btnWipeEdges.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				wipeEdges();
			}
		});

		GButton btnDijkstra = createButton("Dijkstra");
		addButton(btnDijkstra, 3);

		GButton btnKruskal = createButton("Kruskal");
		addButton(btnKruskal, 4);
		btnKruskal.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				doKruskal();
			}
		});

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

	private void addButton(GObject button, int buttonIndex) {
		addAt(button, buttonIndex * 110 + 60, 560);
	}

	protected void addNode() {
		if (edges.mst != null) {
			edges.mst = null;
			if (settings.realtime) {
				doKruskal();
			}
		}
		final NodeView n = new NodeView(settings, new BasicNode<String>(
				Integer.toString(nodes.size() + 1)));
		nodes.add(n);
		graph.add(n.getNode());

		AlphaTween fadein = new AlphaTween(9, 0, 1);
		ScaleTween scalein = new ScaleTween(9, 0, 1.1);
		ScaleTween bounce = new ScaleTween(3, 1.1, 1.0);
		fadein.with(scalein);
		fadein.chain(bounce);
		n.addController(fadein);

		n.setAlpha(0);
		n.setScale(0);
		addAt(n, Math.random() * 700 + 50, Math.random() * 400 + 50);
	}

	public void cancelLink(NodeView nodeView) {
		linkBegin = null;
	}

	private GButton createButton(String text) {
		return ComponentGenerator.attachLabel(
				ComponentGenerator.createButton(), text);
	}

	protected void doKruskal() {
		if (edges.mst == null) {
			Kruskal k = new Kruskal();
			List<WeightedEdge<String, Double>> findMST = k.findMST(graph);
			edges.mst = findMST;
		} else {
			edges.mst = null;
		}
	}

	public void link(NodeView nodeView) {
		if (linkBegin != null) {
			Node<String> head = linkBegin.getNode();
			Node<String> tail = nodeView.getNode();
			gen.nextWeight = settings.useDistanceWeight ? calculateWeightByDistance(
					linkBegin, nodeView) : 1;
			if (graph.connected(head, tail)) {
				graph.unlink(head, tail);
			} else {
				graph.link(head, tail);
			}
			nodeView.setLinking(false);
			linkBegin.setLinking(false);
			linkBegin = null;
			if (edges.mst != null) {
				edges.mst = null;
				if (settings.realtime) {
					doKruskal();
				}
			}
		} else {
			linkBegin = nodeView;
		}
	}

	protected void wipeEdges() {
		edges.mst = null;
		graph.unlinkAll();
	}

	protected void wipeNodes() {
		edges.mst = null;
		for (NodeView n : nodes) {
			remove(n);
			graph.remove(n.getNode());
		}
		nodes.clear();
	}

	private final Listener distanceWeightListener = new FrameListener() {
		@Override
		public void invoke(GObject target, Context context) {
			if (edges.mst != null) {
				edges.mst = null;
				if (settings.realtime) {
					doKruskal();
				}
			}
			NodeView view = (NodeView) target;
			Node<String> node = view.getNode();
			for (WeightedEdge<String, Double> edge : graph.getEdges()) {
				if (!(edge.getTail() == node) && !(edge.getHead() == node)) {
					continue;
				}
				Node<String> otherNode = (node == edge.getTail() ? edge
						.getHead() : edge.getTail());
				NodeView otherView = null;
				for (NodeView couldBeOther : nodes) {
					if (couldBeOther.getNode().equals(otherNode)) {
						otherView = couldBeOther;
						break;
					}
				}
				if (otherView != null) {
					edge.setWeight(calculateWeightByDistance(view, otherView));
				}
			}
		}

	};

	private static double calculateWeightByDistance(NodeView view,
			NodeView otherView) {
		return (view.distanceTo(otherView) - Math.min(
				view.getWidth(),
				Math.min(view.getHeight(),
						Math.min(otherView.getWidth(), otherView.getHeight()))) / 4) / 10;
	}

	public void beginDrag(NodeView nodeView) {
		if (settings.useDistanceWeight) {
			nodeView.addListener(distanceWeightListener);
		}
	}

	public void endDrag(NodeView nodeView) {
		if (settings.useDistanceWeight) {
			nodeView.removeListener(distanceWeightListener);
		}
	}

}
