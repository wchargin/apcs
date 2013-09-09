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

void test(int* counter, bool test, const char * success) {
    if (test) {
        printf("%s\n", success);
        (*counter)++;
    } else {
        printf("fails!\n");
        exit(*counter);
    }
}

int isum(bintree *t, traversal_method m) {
    int i = 0;
    int isum = 0;
    void callback(node *n) {
        isum += (++i) * n -> key;
    }
    
    bt_traverse(t, m, callback);
    return isum;
}

int main() {
    printf("Binary Tree Test Shell\n");
    printf("\n");
    
    int* e = malloc(sizeof(int));
    *e = 1;
    
    printf("Testing binary tree generation... ");
    bintree *t = bt_new();
    test(e, t != NULL, "tree created successfully.");
    
    printf("Testing initial size... ");
    test(e, bt_size(t) == 0, "initial size correct.");
    
    printf("Adding stuff... ");
    // Add {7, 4, 9, 2, 5, 8, 11}
    bt_add(t, 7);
    bt_add(t, 4);
    bt_add(t, 9);
    bt_add(t, 2);
    bt_add(t, 5);
    bt_add(t, 8);
    bt_add(t, 11);
    test(e, true, "successfully added.");
    
    printf("Testing new size... ");
    test(e, bt_size(t) == 7, "new size correct.");
    
    printf("Adding some duplicates... ");
    bt_add(t, 5);
    bt_add(t, 7);
    test(e, bt_size(t) == 7, "size still correct.");
    
    printf("Testing contains (positive case)... ");
    test(e, bt_contains(t, 8), "works fine.");
    
    printf("Testing contains (negative case)... ");
    test(e, !bt_contains(t, 3), "works fine.");
    
    printf("Testing inorder traversal... ");
    // isum should be 1 * 2 + 2 * 4 + 3 * 5 + ... = 224
    test(e, isum(t, INORDER) == 224, "works fine.");
    
    printf("Testing preorder traversal... ");
    // isum should be 1 * 7 + 2 * 4 + 3 * 2 + ... = 217
    test(e, isum(t, PREORDER) == 211, "works fine.");
    
    printf("Testing postorder traversal... ");
    // isum should be 1 * 2 + 2 * 5 + 3 * 4 + ... = 214
    test(e, isum(t, POSTORDER) == 214, "works fine.");
    
    printf("Testing free... ");
    bt_free(t);
    test(e, true, "completes without error.");
    
    printf("Recreating tree... ");
    t = bt_new();
    test(e, true, "completes without error.");
    
    printf("Ensuring zero size... ");
    test(e, bt_size(t) == 0, "indeed.");
    
    printf("Adding a couple nodes, checking size... ");
    bt_add(t, 5);
    bt_add(t, 3);
    bt_add(t, 8);
    test(e, bt_size(t) == 3, "size is correct.");
    
    printf("Ensuring traversal works... ");
    // 1 * 3 + 2 * 5 + 3 * 8 = 37
    test(e, isum(t, INORDER) == 37, "it does.");
    
    printf("\n");
    printf("All tests completed successfully.\n");
    return 0;
}