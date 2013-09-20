package q2;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class CleverTimeConsciousIterator<T> implements BSTIterator<T> {
    
    private Node<T> node;
    private Stack<Node<T>> stack = new Stack<>();
    
    /**
     * Runs in O(1) space and O(1) time.
     */
    public CleverTimeConsciousIterator(BinarySearchTree<T> tree) {
        node = tree.getRoot();
    }
    
    /**
     * Runs in O(0) additional space and O(1) time.
     */
    public Node<T> next() {
        /* Recursive method is:
         *
         * private void inorderTraverse(Node<T> n [[ A: recursion stack ]] {
         *     if (n == null) return;
         *     [[ B ]] inorderTraverse(n.getLeft());  // push left onto stack
         *     [[ C ]] // visit n
         *     [[ D ]]inorderTraverse(n.getRight()); // push right onto stack
         * }
         *
         */
        // If current node is null then visit top from stack
        while (true) if (node == null) {
            // node has no left trees
            // visit
            node = stack.pop();
            
            /* C */ Node<T> toReturn = node;
            /* D */ stack.push(node.getRight());
            return toReturn;
        } else {
            /* A */ stack.push(node);
            /* B */ node = node.getLeft();
        }
    }
    
    /**
     * Runs in O(0) space and O(1) time.
     */
    public boolean hasNext() {
        return !stack.isEmpty();
    }
    
}
