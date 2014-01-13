/*
 * tester.h
 * C test shell framework header file
 * William Chargin
 * 15 September 2013
 */
#ifndef __TESTER_H__
#define __TESTER_H__

#include <stdbool.h>

typedef int* test;

/* create a test */
test t_new();

/* test something */
void _(test i, bool b, const char* success);

/* finish test */
void t_done();

#endif