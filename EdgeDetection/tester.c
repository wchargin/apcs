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
test t_new(const char * string) {
    test t;
    int count;
    
    t = malloc(sizeof(int));
    (*t) = 0;
    
    printf("\n");
    printf("%s%n\n", string, &count);
    for (; count > 0; count--) {
        printf("=");
    }
    printf("\n\n");
    
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