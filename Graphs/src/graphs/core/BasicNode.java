package graphs.core;

public class BasicNode<T> implements Node<T> {

	/**
	 * The value of this node.
	 */
	private final T value;
	
	@Override
	public T getValue() {
		return value;
	}

	public BasicNode(T value) {
		super();
		this.value = value;
	}

}
