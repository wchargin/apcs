/*
 * bitmap.h
 * C bitmap image specification
 * William Chargin
 * 12 January 2014
 */
#ifndef __BITMAP_H__
#define __BITMAP_H__

#include <stdint.h>

/* grayscale image struct declaration */
typedef struct {
    int width;
    int height;
    uint8_t* data;
} grimg_t;

/* create a blank image of specified size */
grimg_t* image_new(int width, int height);

/* access a pixel */
uint8_t image_pixel_read(grimg_t* image, int x, int y);

/* overwrite a pixel */
void image_pixel_write(grimg_t* image, int x, int y, uint8_t paint);

/* fill an image with the specified shade */
void image_fill(grimg_t* image, uint8_t paint);

/* deconstruct an image */
void image_destroy(grimg_t* image);

#endif