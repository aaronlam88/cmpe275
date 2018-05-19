/*DIPs.c*/

#include "DIPs.h"

#ifndef DEBUG
    #define PRINT 0
#else
    #define PRINT 1
#endif

/*check if 0<int<255 functions, if not, replace with 0 or 255: IfLower, IfHigher, Inrange*/
int IfLower(int x)
{
    if (x < 0)
        x = 0;
    return x;
}
int IfHigher(int x)
{
    if (x > 255)
        x = 255;
    return x;
}
int Inrange (int x)
{
    x = IfLower (x);
    x = IfHigher(x);
    return x;
}
/*end of check 0<int<255 function block*/

IMAGE *BlackNWhite(IMAGE *image)
{
    unsigned average;
    int x, y;
    for( y = 0; y < image->Height; y++ )
    {
        for( x = 0; x < image->Width; x++ )
        {
            average = (image->R[x+y*image->Width]+image->G[x+y*image->Width]+image->B[x+y*image->Width])/3;
            SetPixelR(image, x, y, average);
            SetPixelG(image, x, y, average);
            SetPixelB(image, x, y, average);
        }
    }
    if(PRINT){
        printf("\"Black & White\" operation is done!\n");
    }
    return image;
}/*end of BlackNWhite*/

IMAGE
*VFlip(IMAGE *image)
{
    unsigned char temp;
    int x, y;

    int j = image->Height - 1;

    for( y = 0; y < image->Height/2; y++ )
    {
        for( x = 0; x < image->Width; x++ )
        {
            temp                 = GetPixelR(image, x, y);
            SetPixelR(image, x, y, GetPixelR(image, x, j));
            SetPixelR(image, x, j, temp);
            temp                 = GetPixelG(image, x, y);
            SetPixelG(image, x, y, GetPixelG(image, x, j));
            SetPixelG(image, x, j, temp);
            temp                 = GetPixelB(image, x, y);
            SetPixelB(image, x, y, GetPixelB(image, x, j));
            SetPixelB(image, x, j, temp);
        }
        j--;
    }

    if(PRINT){
        printf("\"VFlip\" operation is done!\n");
    }
    return image;
}/*end of VFlip*/

IMAGE
*HMirror(IMAGE *image)
{
    int x, y;
    int i = image->Width-1;

    for( y = 0; y < image->Height; y++ )
    {
        for( x = 0; x < image->Width/2; x++, i--)
        {
            SetPixelR(image, x, y, GetPixelR(image, i, y));
            SetPixelG(image, x, y, GetPixelG(image, i, y));
            SetPixelB(image, x, y, GetPixelB(image, i, y));
        }
        i = image->Width-1;
    }

    if(PRINT){
        printf("\"HMirror\" operation is done!\n");
    }
    return image;
}/*end of HMirror*/

/*EOF DIPs.h*/
