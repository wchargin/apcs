package q2;

public abstract class BinarySearchTree<T> {
    public abstract Node<T> getRoot();
    public abstract Node<T> getSuccessor(Node<T> n);
    public abstract Node<T> getPredecessor(Node<T> n);
    public abstract Node<T> getMinimum();
    public abstract Node<T> getMaximum();
}
