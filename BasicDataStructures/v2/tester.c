/*
 * tester.c
 * C test shell framework implementation
 * William Chargin
 * 15 September 2013
 */
#include <stdlib.h>
#include <stdio.h>
#include "tester.h"

/* create a test */
test t_new() {
    test t;
    t = malloc(sizeof(int));
    (*t) = 0;
    return t;
}

/* test something */
void _(test t, bool b, const char* success) {
    if (b) {
        printf("%s\n", success);
        (*t)++;
    } else {
        printf("\x1B[1;31mfails!\x1B[0m\n\n");
        exit(*t);
    }
}

/* finish test */
void t_done() {
    printf("\n\x1B[1;32mAll tests passed.\x1B[0m\n\n");
}