/*
 * bmtest.c
 * Test shell for bitmap.h implementation
 * William Chargin
 * 13 January 2014
 */
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <stdbool.h>
#include "bitmap.h"
#include "tester.h"

int main() {
    test t;
    grimg_t* im;
    
    /* seed the random number generator */
    srand(time(NULL));
    
    t = t_new("Bitmap Shell");
    
    printf("Creating 100x100 image... ");
    im = image_new(100, 100);
    _(t, im != NULL, "created nonnull object successfully.");
    
    printf("Checking size... ");
    _(t, im -> width == 100 && im -> height == 100, "size is correct.");
    
    printf("Checking nonnull data... ");
    _(t, im -> data != NULL, "data is nonnull.");
    
    printf("Filling with 30%% gray... ");
    image_fill(im, 77);
    _(t, true, "fill completed without error.");
    
    printf("Reading each pixel to verify... "); {
        int i = 100, j = 100;
        bool okay = true;
        while (i --> 0) while (j --> 0) {
            if (image_pixel_read(im, i, j) != 77) {
                okay = false;
            }
        }
        _(t, okay, "everything correct.");
    }
    
    printf("Assigning random values to pixels... "); {
        int i = 100, j = 100;
        while (i --> 0) while (j --> 0) {
            image_pixel_write(im, i, j, rand() % 256);
        }
    }
    _(t, true, "completed without error.");
    
    printf("Deconstructing and recreating image... ");
    image_destroy(im);
    im = image_new(50, 150);
    _(t, im -> width == 50
      && im -> height == 150
      && image_pixel_read(im, 3, 3) == 0, "looks like it worked.");
    
    t_done();
    
    return 0;
}