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

/* random with negatives allowed */
int realrand() {
    return rand() | (rand() & 0x80000000 );
}

bool testUnaryOperation(int count, bool (*test)(type)) {
    /* Test special cases with test({-1, 0, 1}) */
    if (!test(0))  return false;
    if (!test(1))  return false;
    if (!test(-1)) return false;
    
    /* Test random values */
    for (count--; count >= 0; count--) {
        if (!test(realrand())) {
            return false;
        }
    }
    return true;
}

bool testBinaryOperation(int count, bool (*test)(type, type)) {
    /* Test special cases with test({-1, 0, 1}, {-1, 0, 1}) */
    int i, j;
    for (i = -1; i <= 1; i++) {
        for (j = -1; j <= 1; j++) {
            if (!(test(i, j))) {
                return false;
            }
        }
    }
    
    /* Test random values */
    for (count--; count >= 0; count--) {
        if (!test(realrand(), realrand())) {
            return false;
        }
    }
    return true;
}

bool testBitAnd(type x, type y);
bool testBitOr(type x, type y);
bool testIsEqual(type x, type y);
bool testLogicalShift(type x, type n);

bool testBitParity(type x);
bool testLeastBitPos(type x);
bool testBang(type x);
bool testNegate(type x);
bool testAddOk(type x, type y);
bool testIsNonZero(type x);
bool testSM2TC(type x);
bool testMathabs(type x);

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
    
    printf("Testing leastBitPos... ");
    _(t, testUnaryOperation(10000, &testLeastBitPos), "passes.");
    
    printf("Testing bang... ");
    _(t, testUnaryOperation(1000, &testBang), "passes.");
    
    printf("Testing tmax... ");
    _(t, tmax() == 0x7FFFFFFF, "passes.");
    
    printf("Testing negate... ");
    _(t, testUnaryOperation(10000, &testNegate), "passes.");
    
    printf("Testing addOk... ");
    _(t, testBinaryOperation(10000, &testAddOk), "passes.");
    
    printf("Testing isNonZero... ");
    _(t, testUnaryOperation(10000, &testIsNonZero), "passes.");
    
    printf("Testing sm2tc... ");
    _(t, testUnaryOperation(10000, &testSM2TC), "passes.");
    
    printf("Testing mathabs... ");
    _(t, testUnaryOperation(10000, &testMathabs), "passes.");
    
    t_done();
    
    return 0;
}

bool testBitAnd(type x, type y) { return (x & y) == bitAnd(x, y); }
bool testBitOr(type x, type y) { return (x | y) == bitOr (x, y); }
bool testIsEqual(type x, type y) { return (x == y) == isEqual(x, y); }
bool testLogicalShift(type x, type n) {
    type shouldBe, is;
    n = n % 31;
    while (n < 0) {
        n += 31;
    }
    
    shouldBe = ((unsigned)x) >> n;
    is = logicalShift(x, n);
    
    return shouldBe == is;
}
bool testBitParity(type x) {
    type parity = 0;
    type t = x;
    while (t > 0) {
       parity += t & 0x1;
       t >>= 1;
    }
    parity %= 2;
    return parity == bitParity(x);
}
bool testLeastBitPos(type x) {
    int pos;
    type test = leastBitPos(x);
    if (x == 0) {
        return test == 0;
    }
    pos = 0;
    while ((x & 0x1) == 0) {
        x >>= 1;
        pos++;
    }
    return test == (1 << pos);
}
bool testBang(type x) {
    x %= 16; /* distribute more closely to zero */
    return (!x) == bang(x);
}
bool testNegate(type x) {
    return (-x) == negate(x);
}
bool testAddOk(type x, type y) {
    long lsum = (long) x + y;
    return (lsum == (int) lsum) == addOk(x, y);
}
bool testIsNonZero(type x) {
    return (!!x) == isNonZero(x);
}
bool testSM2TC(type x) {
    type sign = x & 0x80000000;
    type magnitude = x & 0x7FFFFFFF;
    type tc = (sign ? (-magnitude) : magnitude);
    
    return sm2tc(x) == tc;
}
bool testMathabs(type x) {
    return (x < 0 ? -x : x) == mathabs(x);
}
