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
    bool successor; // whether to use successor or predecessor for remove
} bintree;

typedef enum traversal_method {
    PREORDER, INORDER, POSTORDER
} traversal_method;

// Create a new binary tree
bintree* bt_new();

bintree* bt_clone(bintree *tree);

int bt_size(bintree *tree);

// contents are equal
bool bt_eq(bintree *t1, bintree *t2);

// contents and structure are equal
bool bt_eqq(bintree *t1, bintree *t2);

void bt_add(bintree *tree, int key);

void bt_remove(bintree *tree, int key);

bool bt_contains(bintree *tree, int key);

void bt_traverse(bintree *tree, traversal_method m, void (*callback) (node*));

int bt_depth(bintree *tree);

// first value is node depth (which is the length of the rest of the array)
int* bt_trace(bintree *tree, int key);

// get all keys at a level
int* bt_atlevel(bintree *tree, int level);

// Make complete binary tree
void bt_optimize(bintree *tree);

void bt_free(bintree *tree);

void bt_freefull(bintree *tree);

#endif