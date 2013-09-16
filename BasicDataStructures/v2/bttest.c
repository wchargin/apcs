/*
 * bttest.c
 * C parentless binary tree test shell
 * William Chargin
 * 16 September 2013
 */
#include <stdlib.h>
#include <stdbool.h>
#include <stdio.h>
#include "bintree.h"
#include "tester.h"

int main() {
    test t; /* used throughout */
    bintree *tree;
    
    t = t_new("Parentless Binary Tree Test Shell");
    
    printf("Creating tree... ");
    tree = bt_new();
    _(t, tree != NULL, "tree is non-null.");
    
    printf("Adding seven values... ");
    bt_add(tree, 5);
    bt_add(tree, 3); bt_add(tree, 2); bt_add(tree, 4);
    bt_add(tree, 7); bt_add(tree, 6); bt_add(tree, 8);
    _(t, tree -> root != NULL, "root is non-null.");
    
    printf("Testing contains... ");
    _(t, bt_contains(tree, 5) &&
         bt_contains(tree, 7) &&
         bt_contains(tree, 2) &&
        !bt_contains(tree, 9) &&
        !bt_contains(tree, 0) &&
        !bt_contains(tree, -7), "works great.");
    
    printf("Removing a value... ");
    bt_remove(tree, 3);
    _(t, !bt_contains(tree, 3), "tree does not contain removed value.");
    
    t_done();
    return 0;
}