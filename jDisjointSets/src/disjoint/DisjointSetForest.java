package disjoint;

/**
 * A disjoint set forest that uses the weighted rank and path compression
 * heuristics.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the type of values held in this disjoint set
 */
public class DisjointSetForest<T> implements
		DisjointSet<DisjointSetForest<T>.Node> {

	/**
	 * A node in the forest.
	 * 
	 * @author William Chargin
	 * 
	 */
	public class Node {

		/**
		 * The value of this node.
		 */
		private final T value;

		/**
		 * The parent node.
		 */
		private Node parent;

		/**
		 * The rank of this node.
		 */
		private int rank;

		/**
		 * Creates this node with the given value, a rank of zero, and itself as
		 * its parent.
		 * 
		 * @param value
		 *            the value for this node
		 */
		private Node(T value) {
			super();
			this.value = value;
			this.parent = this;
			this.rank = 0;
		}

		/**
		 * Gets the value of this node.
		 * 
		 * @return the value of this node
		 */
		public T getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "Node [value=" + value + ", rank=" + rank + "]";
		}
	}

	@Override
	public void makeSet(Node t) {
		t.rank = 0;
	}

	/**
	 * Creates a node in this forest.
	 * 
	 * @param t
	 *            the value for the node
	 * @return a node with the given value
	 */
	public Node makeNode(T t) {
		return new Node(t);
	}

	/**
	 * Creates a node with {@link #makeNode(Object) makeNode} and uses it to
	 * create a set with {@link #makeSet(Node)}. The node is then returned.
	 * 
	 * @param t
	 *            the value for the node
	 * @return a set-initialized node with the given value
	 */
	public Node makeSet(T t) {
		Node n = makeNode(t);
		makeSet(n);
		return n;
	}

	@Override
	public void union(Node x, Node y) {
		if (x == y) {
			return;
		}
		link(findSet(x), findSet(y));
	}

	/**
	 * Links the two disjoint sets of which {@code x} and {@code y} are
	 * representatives.
	 * 
	 * @param x
	 *            the representative of the first set
	 * @param y
	 *            the representative of the second set
	 */
	private void link(Node x, Node y) {
		if (x == y) {
			return;
		}
		if (x.rank > y.rank) {
			y.parent = x;
		} else {
			x.parent = y;
			if (x.rank == y.rank) {
				// weighted rank heuristic
				y.rank++;
			}
		}
	}

	@Override
	public Node findSet(Node t) {
		Node root = t;
		Node traversal = t;
		while (traversal != traversal.parent) {
			traversal = traversal.parent;
		}
		root = traversal;

		// Path compression step
		traversal = t;
		while (traversal != root && traversal.parent != null) {
			// Set the parent to root, then go up to old parent
			Node oldParent = traversal.parent;
			traversal.parent = root;
			traversal = oldParent;
		}
		return root;
	}

}
