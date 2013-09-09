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

void bt_add(bintree* tree, int key) {
    node *n = tree -> root;
    if (n == NULL) {
        // Tree is empty. Create root node.
        tree -> root = (n = mknode(key, value));
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
                // Duplicate
                // Replace value
                n -> value = value;
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

bool bt_contains(bintree *tree, int key) {
    node *n = bth_findnode(tree, key);
    return n != NULL;
}

void bth_traverse(node *n, void(*callback)(node*)) {
    if (n == NULL) {
        return;
    }
    node *left = n -> left;
    node *right = n -> right;
    bth_traverse(left, callback);
    (*callback)(n);
    bth_traverse(right, callback);
}

void bt_traverse(bintree *tree, void(*callback)(node*)) {
    bth_traverse(tree -> root, callback);
}
