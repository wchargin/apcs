/*
 * bits.c
 * CMU 15-213 Lab 1: Twiddling Bits
 * William Chargin
 * 7 January 2014
 */

#define type int

/* unless otherwise specified, allowed operators are: */
/* ! ~ & ^ | + << >> */

/* Part I: Bit Manipulations */

/* duplicate bitwise and (&) using only | and ~ */
/* rating: 1; max ops: 8 */
type bitAnd(type x, type y) {
    
}

/* duplicate bitwise or (|) using only & and ~ */
/* rating: 1; max ops: 8 */
type bitOr(type x, type y) {
    
}

/* returns (x == y ? 1 : 0) */
/* rating: 2; max ops: 5 */
type isEqual(type x, type y) {
    
}

/* does a logical right shift of x by n */
/* rating: 3; max ops: 16 */
type logicalShift(type x, type n) {
    
}

/* returns (x contains odd number of 1s ? 1 : 0) */
/* rating: 4; max ops: 20 */
type bitParity(type x) {
    
}

/* returns a mask that marks the position of least significant 1 bit of x */
/* all other positions of the mask should be zero */
/* rating: 4; max ops: 30 */
type leastBitPos(type x) {
    
}

/* returns a count of the number of 1s in the argument */
/* rating: 4; max ops: 40 */
type bitCount(type x) {
    
}

/* Part II: Two's Complement Arithmetic */

/* returns the largest integer */
/* rating: 1; max ops: 4 */
type tmax() {
    
}

/* compute -x without using - */
/* rating: 2; max ops: 5 */
type negate(x) {
    
}

/* determines whether y can be added to x without overflow */
/* rating: 3; max ops: 20 */
type addOk(type x, type y) {
    
}

/* returns !!x without using ! */
/* rating: 4; max ops: 10 */
type isNonZero(type x) {
    
}

/* converts sign-magnitude to two's-complement */
/* high-order bit of input is sign bit, remainder magnitude */
/* return two's-complement representation */
/* rating: 4; max ops: 15 */
type sm2tc(type x) {
    
}

/* compute x == tmin ? tmin : abs(x) */
/* rating: 4; max ops: 10 */
type abs(type x) {
    
}

/* returns (x + y overflows ? tmax or tmin : x + y) */
/* rating: 4; max ops: 30 */
type satAdd(type x, type y) {
    
}
