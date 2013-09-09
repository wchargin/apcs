/*
 * bttest.c
 * C binary tree test shell
 * William Chargin
 * 9 September 2013
 */
#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include "bintree.h"

void test(int* counter, bool test, const char * success, const char* error) {
    if (test) {
        printf(success);
        (*counter)++;
    } else {
        printf(error);
        exit(*counter);
    }
}

int main() {
    printf("Binary Tree Test Shell\n");
    printf("\n");
    
    int* e = malloc(sizeof(int));
    *e = 1;
    
    printf("Testing binary tree generation... ");
    bintree *t = bt_new();
    test(e, t != NULL, "tree created successfully.\n", "failed to create tree!\n");
    
    printf("Testing initial size... ");
    test(e, bt_size(t) == 0, "initial size correct.\n", "initial size incorrect!\n");
    
    printf("Adding stuff... ");
    // Add {7, 4, 9, 2, 5, 8, 11}
    bt_add(t, 7);
    bt_add(t, 4);
    bt_add(t, 9);
    bt_add(t, 2);
    bt_add(t, 5);
    bt_add(t, 8);
    bt_add(t, 11);
    test(e, true, "successfully added.\n", "failed to add!\n");
    
    printf("Testing new size... ");
    test(e, bt_size(t) == 7, "new size correct.\n", "new size incorrect!\n");
    
    printf("Adding some duplicates... ");
    bt_add(t, 5);
    bt_add(t, 7);
    test(e, bt_size(t) == 7, "size still correct.\n", "size changed!\n");
    
    printf("Testing contains (positive case)... ");
    test(e, bt_contains(t, 8), "works fine.\n", "fails!\n");
    
    printf("Testing contains (negative case)... ");
    test(e, !bt_contains(t, 3), "works fine.\n", "fails!\n");
    
    printf("Testing inorder traversal... "); {
        int i = 0;
        int isum = 0;
        void callback(node *n) {
            isum += (++i) * n -> key;
        }
        
        bt_traverse(t, callback);
        // isum should be 1 * 2 + 2 * 4 + 3 * 5 + ...
        // = 224
        
        test(e, isum == 224, "works fine.\n", "fails!\n");
    }
    
    printf("Testing free... ");
    bt_free(t);
    test(e, true, "completes without error.\n", "fails!\n");
    
    printf("\n");
    printf("All tests completed successfully.\n");
    return 0;
}