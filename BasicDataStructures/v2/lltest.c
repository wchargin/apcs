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
    
    {
        llist *l1;
        llist *l2;
        
        printf("Creating lists that merge... ");
        l1 = ll_new(); l2 = ll_new();
        
        ll_addf(l1, 6); ll_addf(l1, 5); ll_addf(l1, 4);
        /* l1: { 4, 5, 6 } */
        ll_addf(l2, 12); ll_addf(l2, 11); ll_addf(l2, 10);
        /* l2: { 10, 11, 12 } */
        
        {
            /* make last node in l2 point to first node in l1 */
            
            node *n;
            n = l2 -> head;
            while (n -> next != NULL) {
                n = n -> next;
            }
            n -> next = l1 -> head;
        }
        /* l2: { 10, 11, 12, >l1} */
        _(t, true, "done.");
        
        printf("Testing merge with identical length... ");
        _(t, ll_mergeat(l1, l2) -> value == 4, "good.");
        
        printf("Adding a couple items to first list... ");
        ll_addf(l1, 20);
        ll_addf(l1, 21);
        _(t, ll_mergeat(l1, l2) -> value == 4, "still works.");
        
        printf("Adding a bunch of items to second list... ");
        ll_addf(l2, 30); ll_addf(l2, 31); ll_addf(l2, 32); ll_addf(l2, 33);
        _(t, ll_mergeat(l1, l2) -> value == 4, "fine here.");
        
        printf("Adding duplicate value (but not node) to first... ");
        ll_addf(l1, 10);
        _(t, ll_mergeat(l1, l2) -> value == 4, "no confusion.");
        
        printf("Freeing lists... ");
        ll_free(l1);
        ll_free(l2);
        _(t, true, "completed without error.");
    }
    
    t_done();
    return 0;
}