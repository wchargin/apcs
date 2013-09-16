/*
 * llist.c
 * C singly-linked list implementation
 * William Chargin
 * 15 September 2013
 */

#include <stdlib.h> /* for malloc, etc. */
#include "llist.h"

/* create new list properly */
llist* ll_new() {
    llist* l = malloc(sizeof(llist));
    l -> head = NULL;  
    return l;
}

node* ll_mknode(int value) {
    node* n = malloc(sizeof(node));
    n -> value = value;
    n -> next = NULL;
    return n;
}

/* add to front */
void ll_addf(llist* l, int value) {
    node* n; /* node to add */
    n = ll_mknode(value);
    n -> next = l -> head;
    l -> head = n;
}

/* add to back */
void ll_addb(llist* l, int value) {
    node* t; /* traversal node */
    node* n; /* node to add */
    if (l -> head == NULL) {
        /* empty list; use addf */
        ll_addf(l, value);
        return;
    }
    n = ll_mknode(value);
    t = l -> head;
    while (t -> next != NULL) {
        t = t -> next;
    }
    /* t is now last element */
    t -> next = n;
}

/* remove from front */
void ll_popf(llist *l) {
    if (l -> head == NULL) {
        return;
    }
    l -> head = l -> head -> next;
}

/* remove node given previous */
void ll_rmprev(node* prev, node* n) {
    prev -> next = n -> next;
    free(n);
}

/* remove from back */
void ll_popb(llist *l) {
    node* t; /* traversal node */
    if (l -> head == NULL) {
        return;
    }
    t = l -> head;
    if (t -> next == NULL) {
        /* one-element list; use popf */
        ll_popf(l);
    }
    while (t -> next -> next != NULL) {
        t = t -> next;
    }
    /* t is now penultimate node */
    ll_rmprev(t, t -> next);
}

/* get index of the first occurrence */
int ll_find(llist *l, int value) {
    node* t; /* traversal node */
    int i; /* counter */
    t = l -> head;
    if (t == NULL) {
        return -1;
    }
    for (i = 0; t != NULL && t -> value != value; i++) {
        t = t -> next;
    }
    return t == NULL ? /* not in list */ -1 : i;
}

/* remove index */
void ll_rmat(llist *l, int index) {
    node* t; /* traversal node */
    node* p; /* previous node */
    int i; /* counter */
    if (l -> head == NULL) {
        return;
    }
    t = l -> head;
    if (t -> next == NULL && index == 0) {
        /* one-element list; use popf */
        ll_popf(l);
    }
    /* condition includes: t must not be penultimate node */
    for (i = 0; i < index && t != NULL; i++) {
        p = t;
        t = t -> next;
    }
    if (index != i) {
        /* went too far; list does not contain */
        return;
    }
    
    /* t is now penultimate node */
    ll_rmprev(p, t);
}

/* remove first occurrence of value */
void ll_rmval(llist *l, int value) {
    node* t; /* traversal node */
    int i; /* counter */
    if (l -> head == NULL) {
        return;
    }
    t = l -> head;
    if (t -> next == NULL && value == t -> value) {
        /* one-element list; use popf */
        ll_popf(l);
    }
    /* condition includes: t must not be penultimate node */
    /* condition includes: next value must not be searched for */
    for (i = 0; t -> next -> value != value && t -> next -> next != NULL; i++) {
        t = t -> next;
    }
    if (t -> next -> value != value) {
        /* went too far; list does not contain */
        return;
    }
    /* t is now penultimate node */
    ll_rmprev(t, t -> next);
}

/* tests whether the list is cyclic */
bool ll_cycle(llist *l) {
    node* turtle; /* slow node */
    node* rabbit; /* fast node */
    turtle = rabbit = l -> head;
    do {
        /* check */
        if (rabbit == NULL || rabbit -> next == NULL) {
            /* rabbit reached end */
            return false;
        }
        
        /* advance */
        turtle = turtle -> next;
        rabbit = rabbit -> next -> next;
    } while (turtle != rabbit);
    /* turtle == rabbit; cycle found */
    return true;
}