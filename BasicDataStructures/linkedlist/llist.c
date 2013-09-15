/*
 * llist.c
 * C linked-list implementation
 * William Chargin
 * 4 September 2013
 */
#include <stdlib.h>
#include "llist.h"

node *mknode(int value) {
    node *n = malloc(sizeof(node));
    n -> value = value;
    n -> prev = NULL;
    n -> next = NULL;
    return n;
}

int ll_size (llist *l) {
	return l -> len;
}

void ll_push(llist *l, int value) {
	node* n = l -> last;
	if (n == NULL) {
		n = mknode(value);
		l -> first = n;
		l -> last = n;
	} else {
		node *n2 = mknode(value);
		n2 -> prev = n;
		n -> next = n2;
		l -> last = n2;
	}
	l -> len++;
}

int ll_pop(llist *l) {
    return ll_remove(l, l -> len - 1);
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
	l -> len++;
}

int ll_remove(llist *l, int index) {
	node *n = ll_at(l, index);
	node *before = n -> prev;
	node *after = n -> next;
	if (before != NULL) {
		before -> next = after;
	}
	if (after != NULL) {
		after -> prev = before;
	}
	l -> len--;
	
	if (l -> first == n) {
	    l -> first = after;
	}
	if (l -> last == n) {
	    l -> last = before;
	}
	
	return n -> value;
}

void ll_reverse_singlylinked(llist *l) {
    if (ll_size(l) <= 1) {
        // empty can't be reversed,
        // singleton is the reverse of itself
        return;
    }
    node *previous = l -> first;
    node *current = previous -> next;
    previous -> next = NULL;
    node *temp;
    while (current != NULL) {
        temp = current -> next;
        current -> next = previous;
        previous = current;
        l -> first = current;
        current = temp;
    }
}

void ll_reverse(llist *l) {
    // This code is for a doubly linked list.
    // For a singly linked list (which is not how this is implemented) see:
    // ll_reverse_singlylinked(llist*)
    
    if (ll_size(l) <= 1) {
        // empty can't be reversed,
        // singleton is the reverse of itself
        return;
    }
    
    node *current = l -> first;
    node *temp;
    
    while (current != NULL) {
        temp = current -> next;
        
        current -> next = current -> prev;
        current -> prev = temp;
        
        current = temp;
    }
    
    temp = l -> first;
    l -> first = l -> last;
    l -> last = temp;
}
