/*
 * set.c
 * C set implementation
 * William Chargin
 * 4 September 2013
 */
#include <stdlib.h>
#include "set.h"

bool s_add(set *s, int value) {
    if (!s_contains(s, value)) {
        ll_push(s, value);
        return false;
    } else {
        return true;
    }
}

bool s_remove(set *s, int value) {
    int index = ll_seek(s, value);
    if (index != -1) {
        ll_remove(s, index);
        return true;
    } else {
        return false;
    }
}

bool s_contains(set *s, int value) {
    return ll_seek(s, value) != -1;
}

int s_size(set *s) {
    return ll_size(s);
}

set* s_union(set *s1, set *s2) {
    set *dest = malloc(sizeof(set));
    
    node *n;
    n = s1 -> first;
    while (n != NULL) {
        s_add(dest, n -> value);
        n = n -> next;
    }
    n = s2 -> first;
    while (n != NULL) {
        s_add(dest, n -> value);
        n = n -> next;
    }
    return dest;
}

set *s_isect(set *s1, set *s2) {
    set *dest = malloc(sizeof(set));
    
    node *n;
    n = s1 -> first;
    while (n != NULL) {
        if (s_contains(s2, n -> value)) {
            s_add(dest, n -> value);
        }
        n = n -> next;
    }
    
    return dest;
}

set *s_diff(set *s1, set *s2) {
    set *dest = malloc(sizeof(set));
    
    node *n;
    n = s1 -> first;
    while (n != NULL) {
        if (!s_contains(s2, n -> value)) {
            s_add(dest, n -> value);
        }
        n = n -> next;
    }
    
    return dest;
}

set* s_spec(set *s, bool (*p) (int)) {
    set *dest = malloc(sizeof(set));
    
    node *n;
    n = s -> first;
    while (n != NULL) {
        if ((*p)(n -> value)) {
            s_add(dest, n -> value);
        }
        n = n -> next;
    }
    
    return dest;
}