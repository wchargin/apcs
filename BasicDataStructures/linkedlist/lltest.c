/*
 * lltest.c
 * Test suite for the linked-list implementation
 * William Chargin
 * 4 September 2013
 */
#include <stdio.h>
#include <stdlib.h>
#include "llist.h"

void ll_print(llist *l) {
    printf("{ ");
    node *n = l -> first;
    while (n != NULL) {
        printf("%d ", n -> value);
        n = n -> next;
    }
    printf("} [ %d / %d ]\n", (l -> first == NULL ? -999 : l -> first -> value), (l -> last == NULL ? -999 : l -> last -> value));

}

int main(int argc, char** argv) {
    llist *l = malloc(sizeof(llist));
    
    const int reference[11] = {20, 19, 18, 14, 12, 10, 8, 6, 4, 2, -1};
    const int size = sizeof(reference) / sizeof(int);
    
    int error = 1;
    
    // Add numbers 20, 18, 16, 14, ... 2, 0 to the list.
    {
        int i;
        for (i = 20; i >= 0; i -= 2) {
            ll_push(l, i);
        }
    }
    if (ll_size(l) != 11) {
        return error;
    }
    ll_print(l);
    error++;
    
    ll_pop(l);
    ll_push(l, -1);
    ll_insert(l, 1, 19);
    ll_remove(l, 3);
    printf("Phase 2 complete (pop, push, insert, remove).\n");
    
    // Check first and last index.
    if (l -> first -> value != reference[0]) {
        return error;
    }
    printf("Phase 3A complete (check first value).\n");
    error++;
    
    if (l -> last -> value != reference[size - 1]) {
        return error;
    }
    printf("Phase 3B complete (check last value).\n");
    error++;
    
    // Check by traversing.
    {
        node *n = l -> first;
        int index = 0;
        while (n != NULL) {
            if (n -> value == reference[index++]) {
                n = n -> next;
            } else {
                return error;
            }
        }
    }
    printf("Phase 4A complete (traverse forward).\n");
    error++;
    
    // Check by backward traversing.
    {
        node *n = l -> last;
        int index = size - 1;
        while (n != NULL) {
            if (n -> value == reference[index--]) {
                n = n -> prev;
            } else {
                return error;
            }
        }
    }
    printf("Phase 4B complete (traverse backward).\n");
    error++;
    
    // Check by indexing.
    {
        int i;
        for (i = 0; i < ll_size(l); i++) {
            if (reference[i] != ll_get(l, i)) {
                return error;
            }
        }
    }
    printf("Phase 5 complete (iterate).\n");
    error++;
    
    printf("Completed without error.\n");
    return 0;
}