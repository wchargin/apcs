/*
 * stest.c
 * Test suite for the set implementation
 * William Chargin
 * 4 September 2013
 */
#include <stdio.h>
#include <stdlib.h>
#include "set.h"

// predicate function for testing
bool p1(int v);
bool p2(int v);

// image function for testing
int f(int v);

int main(int argc, char** argv) {
    int error = 1;
    
    printf("Set Testing Framework\n");
    printf("\n");

    // Test equality first so we can use it in later tests
    printf("Phase 0: Testing equality methods.\n"); {
        set *s1 = malloc(sizeof(set));
        set *s2 = malloc(sizeof(set));
        int numbers[8] = { -1, 5, 8, 2, 3, 5, 9, 10 };
        int order1 [8] = { 0, 7, 3, 2, 4, 5, 6, 1 };
        int order2 [8] = { 6, 2, 7, 3, 1, 0, 5, 4 };
        int i;
        for (i = 0; i < 8; i++) {
            s_add(s1, numbers[order1[i]]);
            s_add(s2, numbers[order2[i]]);
        }
        bool eq = s_eq(s1, s2);
        if (!eq) {
            return error;
        }
        printf("Equal sets are equal.\n");
        error++;
        
        bool seq = s_subseteq(s1, s2) && s_subseteq(s2, s1);
        if (!seq) {
            return error;
        }
        printf("Subset works both ways.\n");
        error++;
    }
    
    printf("\n");
    printf("Phase I: Testing single-set operations.\n"); {
        set *s = malloc(sizeof(set));
        printf("Set created.\n");
        error++;
        
        printf("Adding numbers.\n");
        s_add(s, 1);
        s_add(s, 0);
        s_add(s, 6);
        s_add(s, 6);
        s_add(s, 1);
        s_add(s, -1);
        // set is now { 1, 0, 6, -1 }    
        
        if (s_size(s) != 4) {
            return error;
        }
        printf("Size correct.\n");
        error++;
        
        printf("Removing numbers.\n");
        s_remove(s, 3); // nothing
        s_remove(s, 1); // -1
        s_remove(s, 6); // -2
        // set is now { 0, -1 }
        
        if (s_size(s) != 2) {
            return error;
        }
        printf("Size still correct.\n");
        error++;
        
        printf("Testing s_contains.\n");
        {
            bool okay = true;
            okay &= s_contains(s, 0);
            okay &= (!s_contains(s, 1));
            okay &= s_contains(s, -1);
            okay &+ (!s_contains(s, 8));
            if (!okay) {
                return error;
            }
        }
        printf("s_contains works as intended.\n");
        error++;
        
    }

    printf("\n");
    printf("Phase II: Testing Multi-Set Operations\n"); {
        set *s1 = malloc(sizeof(set)), *s2 = malloc(sizeof(set));
        printf("Two sets created.\n");
        error++;
        
        s_add(s1, 1);
        s_add(s1, 3);
        s_add(s1, 7);
        
        s_add(s2, 5);
        s_add(s2, -2);
        s_add(s2, 3);
        
        printf("Two sets filled.\n");
        error++;
        
        {
            set *sr_union = s_union(s1, s2);
            printf("Union created.\n");
            error++;
            
            int shouldBe[5] = { -2, 1, 3, 5, 7 };
            int size = sizeof(shouldBe) / sizeof(int);
            if (s_size(sr_union) != size) {
                node *n = sr_union -> first;
                while (n != NULL ){
                    printf("%d ", n -> value);
                    n = n -> next;
                }
                printf("\n");
                printf("Size is %d but should be %d!\n", s_size(sr_union), size);
                return error;
            }
            printf("Size correct (getting good at this).\n");
            error++;
        
            int i;
            for (i = 0; i < size; i++) {
                if (!s_contains(sr_union, shouldBe[i])) {
                    return error;
                }
            }
            printf("Union set contains all required elements.\n");
            error++;
            
        }{
            set *sr_isect = s_isect(s1, s2);
            printf("Intersection created.\n");
            error++;
            
            int shouldBe[1] = { 3 };
            int size = sizeof(shouldBe) / sizeof(int);
            if (s_size(sr_isect) != size) {
                return error;
            }
            printf("Size correct (again).\n");
            error++;
            
            int i;
            for (i = 0; i < size; i++) {
                if (!s_contains(sr_isect, shouldBe[i])) {
                    return error;
                }
            }
            printf("Intersection set contains all required elements.\n");
            error++;
            
        }{
            set *sr_diff = s_diff(s1, s2);
            printf("Difference created.\n");
            error++;
            
            int shouldBe[2] = { 1, 7 };
            int size = sizeof(shouldBe) / sizeof(int);
            if (s_size(sr_diff) != size) {
                return error;
            }
            printf("Size correct (miraculously).\n");
            error++;
            
            int i;
            for (i = 0; i < size; i++) {
                if (!s_contains(sr_diff, shouldBe[i])) {
                    return error;
                }
            }
            printf("Difference set contains all required elements.\n");
            error++;
            
        }
    }
        
    printf("\n");
    printf("Phase III: Testing Specification"); {
        set *s = malloc(sizeof(set));
        printf("Set created.\n");
        error++;
        
        int list[6] = { -2, -1, 1, 4, 8, 9 };
        int size = sizeof(list) / sizeof(int);
        
        int i;
        for (i = 0; i < size; i++) {
            s_add(s, list[i]);
        }
        printf("Set filled.\n");
        error++;
        
        printf("Testing specification with p(x) = (x >= 3).\n"); {
            set *sr_p1 = s_spec(s, p1);
            printf("Specification complete.\n");
            error++;
            
            int expected[3] = { 4, 8, 9 };
            int expectedSize = sizeof(expected) / sizeof(int);
            
            if (s_size(sr_p1) != expectedSize) {
                return error;
            }
            printf("Specification size correct - phew!\n");
            error++;
            
            int j;
            for (j = 0; j < expectedSize; j++) {
                if (!s_contains(sr_p1, expected[j])) {
                    return error;
                }
            }
            printf("Set contains all required elements.\n");
        }
        
        printf("Testing specification with p(x) = (x %% 2 == 0).\n"); {
            set *sr_p1 = s_spec(s, p2);
            printf("Specification complete.\n");
            error++;
            
            int expected[3] = { -2, 4, 8 };
            int expectedSize = sizeof(expected) / sizeof(int);
            
            if (s_size(sr_p1) != expectedSize) {
                return error;
            }
            printf("Specification size correct; what a surprise.\n");
            error++;
            
            int j;
            for (j = 0; j < expectedSize; j++) {
                if (!s_contains(sr_p1, expected[j])) {
                    return error;
                }
            }
            printf("Set contains all required elements.\n");
        }
        
    }
    printf("\n");
    printf("Phase IV: Testing Image\n"); {
        set *s = malloc(sizeof(set));
        set *control  = malloc(sizeof(set));
        set *control2 = malloc(sizeof(set)); // image with predicate lambda x: x >= 3
        int toadd[6]     = { -1,  3,  5, 2, 0, 1 };
        int expected[6]  = {  3, 11, 27, 6, 2, 3 };
        int expected2[6] = {     11, 27          };
        int i;
        for (i = 0; i< 6; i++) {
            s_add(s, toadd[i]);
            s_add(control, expected[i]);
            if (i < 2) s_add(control2, expected2[i]);
        }
        set *image = s_fullimage(s, f);
        bool eq = s_eq(image, control);
        if (!eq) {
            return error;
        }
        printf("Full image set matches control set.\n");
        error++;
        
        set *pimage = s_image(s, p1, f);
        bool peq = s_eq(pimage, control2);
        if (!peq) {
            return error;
        }
        printf("Partial image set matches control set.\n");
        error++;
    }
        
    printf("\n");
    printf("Completed without error.\n");
    return 0;
}

bool p1(int v) {
    return v >= 3;
}
bool p2(int v) {
    return v % 2 == 0;
}
int f(int v) { // image function
    return v * v + 2;
}
