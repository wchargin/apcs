package graphs.core;

import java.util.Random;

public class BasicNode<T> implements Node<T> {

	/**
	 * The value of this node.
	 */
	private T value;
	
	private long random = new Random(0).nextLong();

	public BasicNode(T value) {
		super();
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicNode other = (BasicNode) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return (int) random;
	}

	@Override
	public void setValue(T value) {
		this.value = value;
	}

}
