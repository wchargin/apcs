/*
 * bintree.c
 * C binary search tree implementation
 * William Chargin
 * 7 September 2013
 */
#include <stdlib.h>
#include "bintree.h"

node* mknode(int key, int value) {
    node *n = malloc(sizeof(node));
    n -> key = key;
    n -> value = value;
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

bintree* bt_add(bintree* tree, int key, int value) {
    node *n = tree -> root;
    if (n == NULL) {
        tree -> root = (n = mknode(key, value));
    } else {
        while (true) {
            int nodeKey = n -> key;
            if (key < nodeKey) {
                if (n -> left == NULL) {
                    node *toAdd = mknode(key, value);
                    toAdd -> parent = n;
                    n -> left = toAdd;
                    break;
                } else {
                    n = n -> left;
                    continue;
                }
            } else if (key > nodeKey) {
                if (n -> right == NULL) {
                    node *toAdd = mknode(key, value);
                    toAdd -> parent = n;
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
    }
    tree -> size++;
}

void bt_optimize(bintree *tree) {
    // First, find median element
    // O(log N) time
    double each = 1.0 / tree -> size;
    double average;
    void callback(node *n) {
        average += each * n -> value;
    }
    bt_traverse(tree, callback);
}

void bth_traverse(node *n, void(*callback)(node*)) {
    if (n == NULL) {
        return;
    }
    bth_traverse(n -> left, callback);
    (*callback)(n);
    bth_traverse(n -> right, callback);
}

void bt_traverse(bintree *tree, void(*callback)(node*)) {
    bth_traverse(tree -> root, callback);
}
