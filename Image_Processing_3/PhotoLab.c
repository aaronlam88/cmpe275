/*PhotoLab*/

#include "FileIO.h"
#include "DIPs.h"
#include "Advanced.h"
#include "Timer.h"

/*PrintMenu*/
int 	PrintMenu ();
/* auto test*/
void 	AutoTest(IMAGE *image);

int main()
{
    IMAGE   *image = NULL;

    AutoTest (image);

	/*  end of replacing*/
	return 0;
}

/* auto test*/

void
AutoTest(IMAGE *image)
{
    double wc;
    double us;
    double sys;
    Timer_start();

	char            fname[SLEN] = "UCI_Peter2";
    char            sname[SLEN];

    printf("\n\"Auto Test\" operation started!\n");

    /*option 3*/
    image = ReadImage(fname);
    BlackNWhite(image);
    strcpy(sname, "bw");
    SaveImage(sname, image);
    printf("Black & White tested!\n\n");
    DeleteImage(image);
	/*option 4*/
    image = ReadImage(fname);
    VFlip(image);
    strcpy(sname, "vflip");
    SaveImage(sname, image);
    printf("VFlip tested!\n\n");
    DeleteImage(image);
    /*option 5*/
    image = ReadImage(fname);
    image = HMirror(image);
    strcpy(sname, "hmirror");
    SaveImage(sname, image);
    printf("HMirror tested!\n\n");
    
    Timer_elapsedTime(&wc, &us, &sys);
    printf("wallclock %lf, user %lf, system time %lf\n", wc, us, sys);

}/*end of AutoTest*/

/*EOF PhotoLab.c*/
