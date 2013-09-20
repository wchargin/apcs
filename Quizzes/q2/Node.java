package q2;

public abstract class Node<T> {
    public abstract Node<T> getParent();
    public abstract Node<T> getLeft();
    public abstract Node<T> getRight();
    public abstract T getValue();
}
