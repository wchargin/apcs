package q2;

import java.util.List;
import java.util.ArrayList;

public class GreedyTimeConsciousIterator<T> implements BSTIterator<T> {
    
    private List<Node<T>> l = new ArrayList<>();
    private int index;
    
    /**
     * Runs in O(1) space and O(n) time.
     */
    public GreedyTimeConsciousIterator(BinarySearchTree<T> tree) {
        inorderAdd(tree.getRoot());
    }
    
    private void inorderAdd(Node<T> n) {
        if (n == null) {
            return;
        }
        inorderAdd(n.getLeft());
        l.add(n);
        inorderAdd(n.getRight());
    }
    
    /**
     * Runs in O(0) space and O(1) time.
     */
    public Node<T> next() {
        return l.get(index++);
    }
    
    /**
     * Runs in O(0) space and O(1) time.
     */
    public boolean hasNext() {
        return index < l.size();
    }
    
}
