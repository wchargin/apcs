/*
 * lltest.c
 * C singly-linked list test shell
 * William Chargin
 * 15 September 2013
 */
#include <stdlib.h>
#include <stdbool.h>
#include <stdarg.h>
#include <stdio.h>
#include "llist.h"
#include "tester.h"

bool eq(llist* l, int len, ...) {
    int i;
    node* n;
    int v;
    
    va_list vl;
    va_start(vl, len);
    
    n = l -> head;
    for (i = 0; i < len && n != NULL; i++, n = n -> next) {
        v = va_arg(vl, int);
        if (n -> value != v) {
            va_end(vl);
            return false;
        }
    }
    va_end(vl);
    if (i != len) {
        /* list is shorter */
        return false;
    }
    if (n != NULL) {
        /* list is longer */
        return false;
    }
    return true;
}

int main() {
    test t; /* tester used throughout */
    llist* l;
    t = t_new();
    
    {
        int count;
        printf("\n");
        printf("Singly-Linked List Test Shell%n\n", &count);
        for (; count > 0; count--) {
            printf("=");
        }
        printf("\n\n");
    }
    
    printf("Creating list... ");
    l = ll_new();
    _(t, l != NULL, "list is non-null.");
    
    printf("Pushing three values to front of list... ");
    ll_addf(l, 3);
    ll_addf(l, 2);
    ll_addf(l, 1);
    _(t, eq(l, 3, 1, 2, 3), "list is correct.");
    
    printf("Pushing two values to back of list... ");
    ll_addb(l, 4);
    ll_addb(l, 5);
    _(t, eq(l, 5, 1, 2, 3, 4, 5), "list is correct.");
    
    printf("Pushing another value to front of list... ");
    ll_addf(l, 0);
    _(t, eq(l, 6, 0, 1, 2, 3, 4, 5), "list is correct.");
    
    printf("Popping back three elements... ");
    ll_popb(l); ll_popb(l); ll_popb(l);
    _(t, eq(l, 3, 0, 1, 2), "list is correct.");
    
    printf("Popping first two elements... ");
    ll_popf(l); ll_popf(l);
    _(t, eq(l, 1, 2), "list is correct.");
    
    printf("Adding more elements for further tests... ");
    ll_addf(l, 1);
    ll_addf(l, 0);
    ll_addb(l, 3);
    /* l is now { 0, 1, 2, 3 } */
    _(t, true, "done.");
    
    printf("Testing find... ");
    _(t,
           ll_find(l, 2) == 2  /* middle */
        && ll_find(l, 0) == 0  /* first */
        && ll_find(l, 3) == 3  /* last */
        && ll_find(l, 7) == -1 /* nonexistent */,
      "works fine.");
      
     
    printf("Removing at index 2... ");
    ll_rmat(l, 2);
    _(t, eq(l, 3, 0, 1, 3), "list is correct.");
    
    printf("Removing value 1... ");
    ll_rmval(l, 1);
    _(t, eq(l, 2, 0, 3), "list is correct.");
    
    printf("Adding more elements for further tests... ");
    ll_addb(l, 6);
    ll_addb(l, 9);
    ll_addb(l, 12);
    /* l is now { 0, 3, 6, 9, 12 }*/
    _(t, true, "done.");
    
    printf("Reversing... ");
    ll_reverse(l);
    _(t, eq(l, 5, 12, 9, 6, 3, 0), "list is correct.");
    
    printf("Testing for cycle... ");
    _(t, !ll_cycle(l), "true negative.");
    
    printf("Creating cycle... ");
    l -> head -> next -> next = l -> head;
    _(t, true, "done.");
    
    printf("Testing for cycle... ");
    _(t, ll_cycle(l), "true positive.");
    
    t_done();
    return 0;
}