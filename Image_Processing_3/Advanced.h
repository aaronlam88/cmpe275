#ifndef ADVANCED_H_INCLUDED
#define ADVANCED_H_INCLUDED

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <assert.h>
#include "Image.h"
#include "DIPs.h"
#include "Advanced.h"
/* Posterization */
IMAGE *CallPosterize(IMAGE *image);
IMAGE *Posterize(IMAGE *image, unsigned char rbits, unsigned char gbits, unsigned char bbits);
unsigned char ChangeBit (unsigned char value, unsigned int modifier);

/* add noise to image */
IMAGE *CallAddNoise(IMAGE *image);
IMAGE *AddNoise( int percentage, IMAGE *image);

/* overlay with another image */
IMAGE *CallOverlay (IMAGE *image);
IMAGE *Overlay(char fname[SLEN], IMAGE *image, unsigned int x_offset, unsigned int y_offset);

/*Surprise modification to image*/
IMAGE *Surprise (IMAGE *image);

/*Resize*/
IMAGE *CallResize (IMAGE *image);
IMAGE *Resize( unsigned int percentage, IMAGE *image);

/*Rotate*/
IMAGE *Rotate(IMAGE *image);

/* Juliaset */
IMAGE *CallJuliaset(void);
IMAGE *Juliaset(unsigned int W, unsigned int H, unsigned int max_iteration);

/* BONUS: Crop */
IMAGE *CallCrop (IMAGE *image);
IMAGE *Crop(IMAGE *image, unsigned int x, unsigned int y, unsigned int W, unsigned int H);

/* Test all functions */
void AutoTest(IMAGE *image);


#endif /* ADVANCED_H_INCLUDED*/
