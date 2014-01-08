/*
 * bits.h
 * CMU 15-213 Lab 1: Twiddling Bits header file
 * William Chargin
 * 7 January 2014
 */
#include <stdbool.h>
#define type unsigned int

/* Part I: Bit Manipulations */

type bitAnd(type x, type y);
type bitOr(type x, type y);
type isEqual(type x, type y);
type logicalShift(type x, type n);
type bitParity(type x);
type leastBitPos(type x);
type bitCount(type x);

/* Part II: Two's Complement Arithmetic */
type tmax();
type negate(type x);
type addOk(type x, type y);
bool isNonZero(type x);
type sm2tc(type x);
type mathabs(type x);
type satAdd(type x, type y);
