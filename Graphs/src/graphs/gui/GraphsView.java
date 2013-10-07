package graphs.gui;

import graphs.algorithms.Kruskal;
import graphs.core.AdjacencyGraph;
import graphs.core.BasicNode;
import graphs.core.Edge;
import graphs.core.MutableGraph;
import graphs.core.Node;
import graphs.core.DirectedWeightedEdge;
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
import java.util.List;
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

public class GraphsView extends GContainer {

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

			public void setWeight(int weight) {
				setText(Integer.toString(weight));
			}
		}

		private WeightedEdge<String, Integer> dirtyEdge;
		private Point2D initialPoint;
		private int initialWeight;

		private Collection<? extends Edge<String>> mst;

		private WeightBubble weightMessage;

		{
			addListener(new FrameListener() {
				@Override
				public void invoke(GObject target, Context context) {
					if ((context.getMouseButtonMask() & MouseEvent.BUTTON1_MASK) != 0) {
						if (dirtyEdge == null) {
							Point2D mouse = context.getMouseRelative();

							// find edge within 5px
							Set<? extends WeightedEdge<String, Integer>> graphEdges = graph
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
											for (WeightedEdge<String, Integer> edge : graphEdges) {
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
						Set<? extends WeightedEdge<String, Integer>> neighboringEdges = graph
								.getNeighboringEdges(node);
						for (WeightedEdge<String, Integer> edge : neighboringEdges) {
							if (edge.getTail() == otherNode) {
								Color color = mst != null && mst.contains(edge) ? Color.RED
										: Color.BLACK;

								Color transparent = new Color(
										color.getRGB() & 0x1FFFFFFF, true);
								g.setPaint(new GradientPaint(
										new Point2D.Double(view.getX(), view
												.getY()), color,
										new Point2D.Double(other.getX(), other
												.getY()), transparent));

								float width = (float) (10 * (1f - Math.pow(
										0.8f, Math.abs(edge.getWeight()) / 3f)));
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
	private MutableGraph<String, Node<String>, ? extends WeightedEdge<String, Integer>> graph;

	private NodeView linkBegin;

	{
		WeightedEdgeGenerator<String, Integer, ? extends Edge<String>> gen = DirectedWeightedEdge
				.weightedGenerator();
		gen.nextWeight = 1;
		graph = new AdjacencyGraph<String, Node<String>, WeightedEdge<String, Integer>>(
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
		edges.mst = null;
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
			List<WeightedEdge<String, Integer>> findMST = k.findMST(graph);
			edges.mst = findMST;
		} else {
			edges.mst = null;
		}
	}

	public void link(NodeView nodeView) {
		if (linkBegin != null) {
			edges.mst = null;
			Node<String> head = linkBegin.getNode();
			Node<String> tail = nodeView.getNode();
			if (graph.connected(head, tail)) {
				graph.unlink(head, tail);
			} else {
				graph.link(head, tail);
			}
			nodeView.setLinking(false);
			linkBegin.setLinking(false);
			linkBegin = null;
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

}
