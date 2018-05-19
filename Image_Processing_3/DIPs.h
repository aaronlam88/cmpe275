/*********************************************************/
/* DIPs.h: header file for DIPs module */
/*********************************************************/
#ifndef DIPs_H
#define DIPs_H
#include <stdio.h>
#include "FileIO.h"

/* IfLower and IfHigher will check the int value, if lower than 0 return 0, if higher than 255, return 255 */
int   IfLower (int);
int   IfHigher(int);
/*Inrange call both IfLower and IfHigher to check the value and return the value in the range of 0-255*/
int   Inrange (int);

/* change color image to black & white */
IMAGE *BlackNWhite(IMAGE *image);

/* flip image vertically */
IMAGE *VFlip(IMAGE *image);

/* mirror image horizontally */
IMAGE *HMirror(IMAGE *image);

#endif/*DIPs_H*/
/*EOF DIPs_H*/
