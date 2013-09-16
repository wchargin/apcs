/*
 * bintree.h
 * C parentless binary tree header file
 * William Chargin
 * 16 September 2013
 */
#ifndef __BINTREE_H__
#define __BINTREE_H__

#include <stdbool.h>

typedef struct btnode {
    int value;
    struct btnode* left;
    struct btnode* right;
} btnode;

typedef struct bintree {
    btnode* root;
} bintree;

/* create and initialize new tree */
bintree* bt_new();

/* add to tree */
void bt_add(bintree *tree, int value);

/* remove from tree */
void bt_remove(bintree *tree, int value);

/* test if value is in tree */
bool bt_contains(bintree *tree, int value);

/* find deepest common ancestor */
btnode* bt_ancestor(bintree *tree, int v1, int v2);

#endif
