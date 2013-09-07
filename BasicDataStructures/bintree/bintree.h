/*
 * bintree.h
 * C binary search tree header file
 * William Chargin
 * 7 September 2013
 */
#ifndef __BINTREE_H__
#define __BINTREE_H__

#include <stdbool.h>

typedef struct node {
    int key;
    int value;
    struct node *parent;
    struct node *left;
    struct node *right;
} node;

typedef struct bintree {
    node *root;
    int size;
} bintree;

// Create a new binary tree
bintree* bt_new();

void bt_add(bintree *tree, int key, int value);

void bt_traverse(bintree *tree, void (*callback) (node*));

#endif