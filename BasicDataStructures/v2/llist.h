/*
 * llist.h
 * C singly-linked list header file
 * William Chargin
 * 15 September 2013
 */

#ifndef __LLIST_H__
#define __LLIST_H__

#include <stdbool.h>

typedef struct node {
    int value;
    struct node* next;
} node;

typedef struct llist {
    node *head;
} llist;

/* create new list properly */
llist* ll_new();

/* add to front */
void ll_addf(llist* l, int value);

/* add to back */
void ll_addb(llist* l, int value);

/* remove from front */
void ll_popf(llist *l);

/* remove from back */
void ll_popb(llist *l);

/* get index of the first occurrence */
int ll_find(llist *l, int value);

/* remove index */
void ll_rmat(llist *l, int index);

/* remove first occurrence of value */
void ll_rmval(llist *l, int value);

/* tests whether the list is cyclic */
bool ll_cycle(llist *l);

/* reverse list */
void ll_reverse(llist *l);

#endif
