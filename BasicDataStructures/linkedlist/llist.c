/*
 * llist.c
 * C linked-list implementation
 * William Chargin
 * 4 September 2013
 */
#ifndef __LLIST_C__
#define __LLIST_C__
#include <stdio.h>
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

int ll_size (llist *l) {
	return l -> len;
}

void ll_push(llist *l, int value) {
	node* n = l -> last;
	if (n == NULL) {
		n = malloc(sizeof(node));
		n -> value = value;
		l -> first = n;
		l -> last = n;
	} else {
		node* n2 = malloc(sizeof(node));
		n2 -> value = value;
		n2 -> prev = n;
		n -> next = n2;
		l -> last = n2;
	}
	l -> len++;
}

node* ll_at(llist *l, int index) {
	node *n;
	if (index < (l -> len) >> 1) {
		n = l -> first;
		int i;
		for (i = 0; i<index; i++) {
			n = n -> next;
		}
	} else {
		n = l -> last;
		int i;
		int len = l -> len;
		for (i = 0; i < len - index - 1; i++) {
			n = n -> prev;
		}
	}
	return n;
}

node* ll_pop(llist *l, int index) {
	node *n = ll_at(l, index);
	node *before = n -> prev;
	node *after = n -> next;
	if (before != NULL) {
		before -> next = after;
	}
	if (after != NULL) {
		after -> prev = before;
	}
	return n;
}

int ll_get(llist *l, int index) {
	return ll_at(l, index) -> value;
}

int ll_set(llist *l, int index, int value) {
	node *n = ll_at(l, index);
	int prev = n -> value;
	n -> value = value;
	return prev;
}

int ll_seek(llist *l, int value) {
	node *n = l -> first;
	int index = 0;
	while (n != NULL && n -> value != value) {
		n = n -> next;
		index++;
	}
	return n == NULL ? -1 : index;
}

void ll_insert(llist *l, int index, int value) {
	node *n = malloc(sizeof(node));
	n -> value = value;

	node *after = ll_at(l, index);
	node *before = after -> prev;
	if (before != NULL) {
		before -> next = n;
	}
	after -> prev = n;
	n -> next = after;
	n -> prev = before;
}

#endif
