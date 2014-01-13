/*
 * bitmap.h
 * C bitmap image implementation
 * William Chargin
 * 12 January 2014
 */
#include <stdlib.h>
#include <string.h> /* memset */
#include "bitmap.h"

/* create a blank image of specified size */
grimg_t* image_new(int width, int height) {
    grimg_t* im = malloc(sizeof(grimg_t));
    im -> width = width;
    im -> height = height;
    im -> data = malloc(width * height * sizeof(uint8_t));
    return im;
}

/* access a pixel */
uint8_t image_pixel_read(grimg_t* image, int x, int y) {
    return (image -> data)[x + y * image -> width];
}

/* overwrite a pixel */
void image_pixel_write(grimg_t* image, int x, int y, uint8_t paint) {
    (image -> data)[x + y * image -> width] = paint;
}

/* fill an image with the specified shade */
void image_fill(grimg_t* image, uint8_t paint) {
    int i = image -> width, j = image -> height;
    while (i --> 0) {
        while (j --> 0) {
            image_pixel_write(image, i, j, paint);
        }
    }
}

/* deconstruct an image */
void image_destroy(grimg_t* image) {
    memset(image -> data, 0, image->width * image->height);
    image -> width = 0;
    image -> height = 0;
    free(image -> data);
    image -> data = 0;
    free(image);
}
