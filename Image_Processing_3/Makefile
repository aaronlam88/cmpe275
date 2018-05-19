#####################################################
# 11/17/13 Aaron Lam: Modify to be use in HW4 EECS 22 F13
# Makefile: Makefile for Assignment3 EECS22 Fall2012
# 10/27/13 Yasaman Samei: updated for HW3 EECS22 F13
# 10/16/11 Weiwei Chen: updated for EECS22 Fall2012 
# 10/10/11 Weiwei Chen: initial solution version                  
#####################################################

# design names
DESIGN = PhotoLab PhotoLabTest

#libs
LIBS   = libfilter.a libfileIO.a 

CC     = gcc-8
DEBUG  = -g -DDEBUG
CFLAGS = -Wall -c -fopenmp
LFLAGS = -Wall -fopenmp
AR     = ar rc
RANLIB = ranlib

IMAGES = bw.ppm negative.ppm hflip.ppm hmirror.ppm vflip.ppm vmirror.ppm \
		 aging.ppm blur.ppm edge.ppm sharpen.ppm border.ppm colorfilter.ppm noise.ppm overlaybat.ppm overlayturkey.ppm\
		 poster.ppm bigresize.ppm crop.ppm juliaset.ppm rotate.ppm smallresize.ppm

OMP = PhotoLabTest-omp PhotoLabTest-omp.dSYM PhotoLabTest.dSYM
		  
all: PhotoLab   PhotoLabTest PhotoLabTest-omp

#
Timer.o: Timer.h Timer.c
	$(CC) $(CFLAGS) Timer.c -o Timer.o

#target to generate PhotoLab.o
PhotoLab.o: PhotoLab.c
	$(CC) $(CFLAGS) PhotoLab.c -o PhotoLab.o 

#target to generate FileIO.o
FileIO.o: FileIO.h FileIO.c
	$(CC) $(CFLAGS) FileIO.c -o FileIO.o

#target to generate DIPs.o
DIPs.o: DIPs.h DIPs.c
	$(CC) $(CFLAGS)  DIPs.c -o DIPs.o

#target to generate DIPs-omp.o
DIPs-omp.o: DIPs.h DIPs-omp.c
	$(CC) $(CFLAGS)  DIPs-omp.c -o DIPs-omp.o

#target to generate Advanced.o
Advanced.o: Advanced.h Advanced.c
	$(CC) $(CFLAGS) -c Advanced.c -o Advanced.o

#target to generate Image.o
Image.o: Image.h Image.c
	$(CC) $(CFLAGS) -c Image.c -o Image.o

#target to generate libfileIO.a
libfileIO.a: FileIO.o
	$(AR) libfileIO.a FileIO.o
	$(RANLIB) libfileIO.a

#target to generate libfilter.a
libfilter.a: DIPs.o Advanced.o Image.o Timer.o
	$(AR) libfilter.a Advanced.o DIPs.o Image.o Timer.o
	$(RANLIB) libfilter.a

#target to generate PhotoLab
PhotoLab: PhotoLab.o libfileIO.a libfilter.a
	$(CC) $(LFLAGS) PhotoLab.o -lfileIO -lfilter -L. -o PhotoLab

Timer_DEBUG.o: Timer.h Timer.c
	$(CC) $(CFLAGS) $(DEBUG) -c Timer.c -o Timer_DEBUG.o

#target to generate FileIO_DEBUG.o
FileIO_DEBUG.o: FileIO.h FileIO.c
	$(CC) $(CFLAGS) $(DEBUG) -c FileIO.c -o FileIO_DEBUG.o 

#target to generate Advanced_DEBUG.o
Advanced_DEBUG.o: Advanced.h Advanced.c
	$(CC) $(CFLAGS) $(DEBUG) -c Advanced.c -o Advanced_DEBUG.o

#target to generate Image.o
Image_DEBUG.o: Image.h Image.c
	$(CC) $(CFLAGS) $(DEBUG) -c Image.c -o Image_DEBUG.o

#target to generate FileIO_DEBUG.o
PhotoLab_DEBUG.o: PhotoLab.c
	$(CC) $(CFLAGS) $(DEBUG) -c PhotoLab.c -o PhotoLab_DEBUG.o 

#target to generate test
PhotoLabTest:  PhotoLab_DEBUG.o FileIO_DEBUG.o DIPs.o Advanced_DEBUG.o Image_DEBUG.o Timer_DEBUG.o
	$(CC)  $(LFLAGS) $(DEBUG) PhotoLab_DEBUG.o FileIO_DEBUG.o DIPs.o Advanced_DEBUG.o Image_DEBUG.o Timer_DEBUG.o -o PhotoLabTest

#target to genarte test with omp
PhotoLabTest-omp:  PhotoLab_DEBUG.o FileIO_DEBUG.o DIPs-omp.o Advanced_DEBUG.o Image_DEBUG.o Timer_DEBUG.o
	$(CC)  $(LFLAGS) $(DEBUG) PhotoLab_DEBUG.o FileIO_DEBUG.o DIPs-omp.o Advanced_DEBUG.o Image_DEBUG.o Timer_DEBUG.o -o PhotoLabTest-omp
	
#target to clean the directory
clean:
	rm -rf *.o *.jpg $(DESIGN) $(IMAGES) $(LIBS) $(OMP)
    