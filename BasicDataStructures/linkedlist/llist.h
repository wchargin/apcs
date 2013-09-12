/*
 * llist.c
 * C linked-list header file
 * William Chargin
 * 4 September 2013
 */
#ifndef __LLIST_H__
#define __LLIST_H__
#include <stdlib.h>

typedef struct node {
	int value;
	struct node* next;
	struct node* prev;
} node;

typedef struct llist {
	int len;
	struct node* first;
	struct node* last;
} llist;

int ll_size (llist *l);

void ll_push(llist *l, int value);

int ll_pop(llist *l);

node* ll_at(llist *l, int index);

int ll_get(llist *l, int index);

int ll_set(llist *l, int index, int value);

int ll_seek(llist *l, int value);

void ll_insert(llist *l, int index, int value);

int ll_remove(llist *l, int index);

// reverse in-place
void ll_reverse(llist *l);

#endif
