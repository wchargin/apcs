/*
 * btest.c
 * Test shell for bitlab
 * William Chargin
 * 7 January 2014 
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "bits.h"
#include "tester.h"

bool testUnaryOperation(int count, bool (*test)(int)) {
    for (count--; count >= 0; count--) {
        if (!test(rand())) {
            return false;
        }
    }
    return true;
}

bool testBinaryOperation(int count, bool (*test)(int, int)) {
    for (count--; count >= 0; count--) {
        if (!test(rand(), rand())) {
            return false;
        }
    }
    return true;
}

bool testBitAnd(int x, int y);
bool testBitOr(int x, int y);
bool testIsEqual(int x, int y);
bool testLogicalShift(int x, int n);

bool testBitParity(int x);

int main() {
    test t;
    
    /* Initialize random number generator */
    srand(time(NULL));
    
    t = t_new("Bitlab Shell");
    
    printf("Testing bitAnd... ");
    _(t, testBinaryOperation(10000, &testBitAnd), "passes.");
    
    printf("Testing bitOr... ");
    _(t, testBinaryOperation(10000, &testBitOr), "passes.");
    
    printf("Testing isEqual... ");
    _(t, testBinaryOperation(10000, &testIsEqual), "passes.");
    
    printf("Testing logicalShift... ");
    _(t, testBinaryOperation(10000, &testLogicalShift), "passes.");
    
    printf("Testing bitParity... ");
    _(t, testUnaryOperation(10000, &testBitParity), "passes.");
    
    t_done();
    
    return 0;
}

bool testBitAnd(int x, int y) { return (x & y) == bitAnd(x, y); }
bool testBitOr(int x, int y) { return (x | y) == bitOr (x, y); }
bool testIsEqual(int x, int y) { return (x == y) == isEqual(x, y); }
bool testLogicalShift(int x, int n) {
    n = n % 31;
    return (x >> n) == logicalShift(x, n);
}
bool testBitParity(int x) {
    /* from http://stackoverflow.com/a/7531124 */
    int parity = 0;
    int t = x;
    while (t > 0) {
       parity = (parity + (t & 1)) % 2;
       t >>= 1;
    }
    return parity == bitParity(x);
}
