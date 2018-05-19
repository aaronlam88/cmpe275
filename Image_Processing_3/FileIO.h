/*********************************************************************/
/* FileIO.h: header file for I/O module	                             */
/*                                                                   */
/* 10/29/13 Alex Chu  updated for EECS22 assignment4 Fall2013	     */
/* 10/30/12 Weiwei Chen  updated for EECS22 assignment4 FAll2012     */
/* 10/07/11 Weiwei Chen: initial solution version                    */
/*                       for EECS22 assignment3 FAll2011             */
/*********************************************************************/
#ifndef FILE_IO_H
#define FILE_IO_H
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "Image.h"
#include "Constants.h"

/* read image from a file 						*/
/* the size of the image needs to be pre-set				*/
/* the memory spaces of the image will be allocated in this function	*/
/* return values:							*/
/* NULL: fail to load or create an image				*/
/* image: load or create an image successfully				*/
IMAGE *ReadImage(const char *fname);

/* save a processed image 		*/
/* return values:			*/
/* 0: successfully saved the image	*/
/* 1: Cannot open the file for writing	*/
/* 2: File error while writing to file	*/
int SaveImage(const char *fname, IMAGE *image);

#endif
