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
        !bt_contains(tree, -7), "works fine.");
    
    printf("Removing a value... ");
    bt_remove(tree, 3);
    _(t, bt_contains(tree, 5) &&
         bt_contains(tree, 7) &&
         bt_contains(tree, 2) &&
        !bt_contains(tree, 9) &&
        !bt_contains(tree, 3) &&
        !bt_contains(tree, -7), "works great.");
    
    printf("Finding deepest ancestor of siblings... ");
    _(t, bt_ancestor(tree, 6, 8) -> value == 7, "correct.");
    
    printf("Finding deepest ancestor of distants... ");
    _(t, bt_ancestor(tree, 2, 8) -> value == 5, "correct.");
    
    printf("Finding deepest ancestor of nonexistent... ");
    _(t, bt_ancestor(tree, 2, 20) == NULL, "correctly null.");
    
    printf("Finding deepest ancestor of two nonexistents... ");
    _(t, bt_ancestor(tree, 50, 60) == NULL, "correctly null.");
    
    printf("Finding successor node... ");
    _(t, bt_successor(tree, tree->root->right->left)->value == 7, "correct.");
    
    printf("Finding predecessor node... ");
    _(t, bt_predecessor(tree, tree->root->right->left)->value==5, "correct.");
    
    printf("Adding numbers for further tests... "); {
        int i;
        for (i = 0; i < 30 /* (2^n - 1) - 1 */; i++) {
            /* will contain some duplicates but that's okay */
            /* duplicates just won't be added to the tree */
            bt_add(tree, i);
        }
    }
    _(t, true, "done.");
    
    printf("Converting to linked list... "); {
        btnode *head = NULL;
        head = bt_tolist(tree);
        _(t, head != NULL, "completed without error.");
        
        printf("Checking forward traversal... "); {
            btnode *n = head;
            bool okay = true;
            int i = 0;
            do {
                if (n -> value != i) {
                    okay = false;
                    break;
                }
                n = n -> right;
                i++;
            } while (n != head);
            _(t, okay && i == 30, "works fine.");
        }
        printf("Checking backward traversal... "); {
            btnode *n = head -> left;
            bool okay = true;
            int i = 29;
            do {
                if (n -> value != i) {
                    okay = false;
                    break;
                }
                n = n -> left;
                i--;
            } while (n != head -> left);
            _(t, okay && i == -1, "works fine.");
        }
    }
    
    t_done();
    return 0;
}
