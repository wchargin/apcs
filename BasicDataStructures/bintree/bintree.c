/*
 * bintree.c
 * C binary search tree implementation
 * William Chargin
 * 7 September 2013
 */
#include <stdlib.h>
#include "bintree.h"

node* mknode(int key) {
    node *n = malloc(sizeof(node));
    n -> key = key;
    n -> parent = NULL;
    n -> left = NULL;
    n -> right = NULL;
}

bintree* bt_new() {
    // allocate and zero out
    bintree *tree = malloc(sizeof(bintree));
    tree -> root = NULL;
    tree -> size = 0;
}

int bt_size(bintree *tree) {
    return tree -> size;
}

void bt_add(bintree* tree, int key) {
    node *n = tree -> root;
    if (n == NULL) {
        // Tree is empty. Create root node.
        tree -> root = (n = mknode(key));
    } else {
        node *toAdd = mknode(key);
        while (true) {
            int nodeKey = n -> key;
            if (key < nodeKey) {
                if (n -> left == NULL) {
                    n -> left = toAdd;
                    break;
                } else {
                    n = n -> left;
                    continue;
                }
            } else if (key > nodeKey) {
                if (n -> right == NULL) {
                    n -> right = toAdd;
                    break;
                } else {
                    n = n -> right;
                    continue;
                }
            } else {
                // Already exists
                return; // so that size is not incremented
            }
        }
        toAdd -> parent = n;
    }
    tree -> size++;
}

node* bth_findnode(bintree *tree, int key) {
    node *n = tree -> root;
    while (n != NULL) {
        int nodeKey = n -> key;
        if (key < nodeKey) {
            n = n -> left;
        } else if (key > nodeKey) {
            n = n -> right;
        } else {
            return n;
        }
    }
    return NULL;
}



void bt_remove(bintree *tree, int key) {
    node *n = bth_findnode(tree, key);
    if (n == NULL) {
        // Not in tree
        return;
    }
    
    if (n -> left == NULL && n -> right == NULL) {
        // Node has no children.
        // We can safely remove.
        
        if (n -> parent == NULL) {
            // Node is root
            tree -> root = NULL;
        } else {
            // Node is leaf
            *((n == n -> parent -> left)
            ? &(n -> parent -> left)
            : &(n -> parent -> right)) = NULL;
        }
    } else
    
    if (n -> left == NULL ^ n -> right == NULL) {
        // Node has one child
        // We can replace this with child
        
        bool childIsLeft  = n -> left != NULL;
        bool parentIsLeft = n == n -> parent -> left;
        
        *((parentIsLeft)
        ? &(n -> parent -> left)
        : &(n -> parent -> right)) = (childIsLeft ? n -> left : n -> right);
    }
    
    else {
        // Node has two children.
        node *toReplace;
        if (tree -> successor) {
            toReplace = n -> right;
            while (toReplace -> left != NULL) {
                toReplace = toReplace -> left;
            }
        } else {
            toReplace = n -> left;
            while (toReplace -> right != NULL) {
                toReplace = toReplace -> right;
            }
        }
        n -> key = toReplace -> key;
        
        // we looped until toReplace had (at least) one dead end
        // we can now remove it with one of the above operations
        bt_remove(tree, toReplace -> key);
        
        tree -> successor = !(tree -> successor);
    }

    free(n);
    tree -> size--;
}

bool bt_contains(bintree *tree, int key) {
    node *n = bth_findnode(tree, key);
    return n != NULL;
}

void bth_traverse(node *n, traversal_method m, void(*callback)(node*)) {
    if (n == NULL) {
        return;
    }
    node *left = n -> left;
    node *right = n -> right;
    
    switch (m) {
    case PREORDER:
        (*callback)(n);
        bth_traverse(left, m, callback);
        bth_traverse(right, m, callback);
        break;
    case INORDER:
        bth_traverse(left, m, callback);
        (*callback)(n);
        bth_traverse(right, m, callback);
        break;
    case POSTORDER:
        bth_traverse(left, m, callback);
        bth_traverse(right, m, callback);
        (*callback)(n);
        break;
    }
}

void bt_traverse(bintree *tree, traversal_method m, void(*callback)(node*)) {
    bth_traverse(tree -> root, m, callback);
}

int bth_nodedepth(node *n) {
    if (n == NULL) {
        return 0;
    }
    int left = bth_nodedepth(n -> left);
    int right = bth_nodedepth(n -> right);
    return 1 /* for this node */ + (left > right ? left : right);
}

int bt_depth(bintree *tree) {
    return bth_nodedepth(tree -> root);
}

void bt_free(bintree *tree) {
    void freenode(node *n) {
        free(n);
    }
    // free each node then free the tree
    bt_traverse(tree, INORDER, freenode);
    free(tree);
}
