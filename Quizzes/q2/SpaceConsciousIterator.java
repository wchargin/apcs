package q2;

public class SpaceConsciousIterator<T> implements BSTIterator<T> {
    
    private Node<T> lastVisited;
    private BinarySearchTree<T> tree;
    
    /**
     * Runs in O(1) space and equal time as BinarySearchTree#getMinimum().
     */
    public SpaceConsciousIterator(BinarySearchTree<T> tree) {
        this.tree = tree;
        this.lastVisited = tree.getMinimum();
    }
    
    /**
     * Runs in O(1) space and equal time as BinarySearchTree#getSuccessor(Node).
     */
    public Node<T> next() {
        Node<T> temp = lastVisited;
        lastVisited = tree.getSuccessor(lastVisited);
        return temp;
    }
    
    /**
     * Runs in O(0) space and O(1) time.
     */
    public boolean hasNext() {
        return lastVisited != null;
    }
    
}
