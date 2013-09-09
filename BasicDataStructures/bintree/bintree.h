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

int bt_size(bintree *tree);

void bt_add(bintree *tree, int key);

bool bt_contains(bintree *tree, int key);

void bt_traverse(bintree *tree, void (*callback) (node*));

void bt_free(bintree *tree);

#endif