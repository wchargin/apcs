/*
 * llmain.c
 * Main shell for linked-list class
 * William Chargin
 * 4 September 2013
 */
#include <stdio.h>
#include <stdlib.h>
#include "llist.c"

void traverse(llist *l);

int main(int argc, char** argv) {
	printf("Creating list.\n");
	llist *l  = malloc(sizeof(llist));
	printf("List size: %d.\n", ll_size(l));
	printf("Adding 1, 2, 3, 4, 5 to list.\n");
	int n;
	for (n = 1; n <= 5; n++) {
		ll_push(l, n);
	}
	printf("List size: %d.\n", ll_size(l));

	printf("\n");
	printf("Iterating.\n");
	for (n = 0; n < ll_size(l); n++) {
		printf("Value at index %d: %d\n", n, ll_get(l, n));
	}

	traverse(l);

	printf("\n");
	printf("Indexing.\n");
	for (n = 1; n <= 5; n++) {
		printf("Index of %d is %d.\n", n, ll_seek(l, n));
	}

	printf("\n");
	printf("Removing index 2.\n");
	printf("Index 2 was %d.\n", ll_pop(l, 2) -> value);

	traverse(l);

	printf("\n");
	printf("Adding 10 at index 3.\n");
	ll_insert(l, 3, 10);
	traverse(l);
}

void traverse(llist *l) {
	printf("\n");
	printf("Traversing.\n");
	node *no = l -> first;
	while (no != NULL) {
		printf("%d ", no -> value);
		no = no -> next;
	}
	printf("\n");

	printf("\n");
	printf(".gnisrevarT\n");
	no = l -> last;
	while (no != NULL) {
		printf("%d ", no -> value);
		no = no -> prev;
	}
	printf("\n");

}
