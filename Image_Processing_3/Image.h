/*********************************************************************/
/* Image.h: header file for basic image manipulations		     */
/*                                                                   */
/* 10/29/13 Alex Chu  updated for EECS22 assignment4 Fall2013	     */
/* 10/30/12 Weiwei Chen  updated for EECS22 assignment4 FAll2012     */
/* 10/20/11 Weiwei Chen: initial solution version                    */
/*                       for EECS22 assignment4 FAll2011             */
/*********************************************************************/
#ifndef IMAGE_H
#define IMAGE_H
#include <assert.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

typedef struct {
	unsigned int Width;	/* image width */
	unsigned int Height; 	/* image height */
	unsigned char *R;	/* pointer to the memory storing all the R intensity values */
	unsigned char *G;	/* pointer to the memory storing all the G intensity values */
	unsigned char *B;	/* pointer to the memory storing all the B intensity values */
}IMAGE;

/* Get the color intensity of the Red channel of pixel (x, y) in image */
unsigned char GetPixelR(IMAGE *image, unsigned int x,  unsigned int y);

/* Get the color intensity of the Green channel of pixel (x, y) in image */
unsigned char GetPixelG(IMAGE *image, unsigned int x,  unsigned int y);

/* Get the color intensity of the Blue channel of pixel (x, y) in image */
unsigned char GetPixelB(IMAGE *image, unsigned int x,  unsigned int y);

/* Set the color intensity of the Red channel of pixel (x, y) in image with value r */
void SetPixelR(IMAGE *image, unsigned int x,  unsigned int y, unsigned char r);

/* Set the color intensity of the Green channel of pixel (x, y) in image with value g */
void SetPixelG(IMAGE *image, unsigned int x,  unsigned int y, unsigned char g);

/* Set the color intensity of the Blue channel of pixel (x, y) in image with value b */
void SetPixelB(IMAGE *image, unsigned int x,  unsigned int y, unsigned char b);

/* allocate the memory space for the image structure         */
/* and the memory spaces for the color intensity values.     */
/* return the pointer to the image, or NULL in case of error */
IMAGE *CreateImage(unsigned int Width, unsigned int Height);

/* release the memory spaces for the pixel color intensity values */
/* deallocate all the memory spaces for the image                 */
void DeleteImage(IMAGE *image);

#endif
