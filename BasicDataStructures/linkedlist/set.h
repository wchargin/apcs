/*
 * set.h
 * C set header file
 * William Chargin
 * 4 September 2013
 */
#ifndef __SET_H__
#define __SET_H__
#include <stdlib.h>
#include <stdbool.h> // seriously have to do this??
#include "llist.h"

// For semantics
typedef struct llist set;

// These three should return true iff $value \in s$ before the method call.
bool s_add(set *s, int value);
bool s_remove(set *s, int value);
bool s_contains(set *s, int value);

// delegate
int s_size(set *s);

// Set operations
set* s_union(set *s1, set *s2);
set* s_isect(set *s1, set *s2);
set* s_diff(set *s1, set *s2); // s1 \ s2; ${x \in s1 | x \notin s2}$

// Axiom of Specification; see ZFC theory
set* s_spec(set *s, bool (*p) (int));

// Set tests
bool s_subseteq(set *s1, set *s2);
bool s_eq(set *s1, set *s2);


#endif