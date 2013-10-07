package disjoint;

/**
 * A linked-list implementation of a disjoint set.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the type of values held in this disjoint set
 */
public class LinkedDisjointSet<T> implements
		DisjointSet<LinkedDisjointSet<T>.Node> {

	/**
	 * A node in the linked list.
	 * 
	 * @author William Chargin
	 * 
	 */
	public class Node {

		/**
		 * The value stored in this node.
		 */
		public final T value;

		/**
		 * The next node in the linked list.
		 */
		private Node next;

		/**
		 * The representative of the set to which this node belongs.
		 */
		private Node representative;

		/**
		 * Creates a new node with the given value, no {@code next} pointer, and
		 * itself as its representative.
		 * 
		 * @param value
		 */
		private Node(T value) {
			super();
			this.value = value;
			this.next = null;
			this.representative = this;
		}

		/**
		 * Gets the value stored in this node.
		 * 
		 * @return the value
		 */
		public T getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "Node [value=" + value + "]";
		}
	}

	@Override
	public Node findSet(Node t) {
		return t.representative;
	}

	/**
	 * Creates a node in this list.
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
	public void makeSet(Node t) {
		t.next = null;
		t.representative = t;
	}

	@Override
	public void union(Node x, Node y) {
		Node xHead = findSet(x);
		Node yHead = findSet(y);

		if (xHead == yHead) {
			return;
		}
		
		for (Node i = yHead; i != null; i = i.next) {
			i.representative = xHead;
		}
		x = xHead;
		while (x.next != null) {
			x = x.next;
		}
		x.next = yHead;
	}
}
