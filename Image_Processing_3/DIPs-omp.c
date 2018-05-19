/*DIPs.c*/

#include "DIPs.h"
#include "omp.h"

#define NUM_THREADS 4

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
int Inrange(int x)
{
    x = IfLower(x);
    x = IfHigher(x);
    return x;
}
/*end of check 0<int<255 function block*/

IMAGE *BlackNWhite(IMAGE *org_image)
{
    omp_set_dynamic(0);     // Explicitly disable dynamic teams
    omp_set_num_threads(NUM_THREADS); // Use NUM_THREADs threads for all consecutive parallel regions
    const unsigned int width = org_image->Width;
    const unsigned int height = org_image->Height;
    int id = omp_get_thread_num();
    IMAGE *image = org_image;
#pragma omp parallel
    {
        for (int y = id; y < height; y += NUM_THREADS)
        {
            for (int x = 0; x < width; x+= NUM_THREADS)
            {
                unsigned int average = (image->R[x + y * width] + image->G[x + y * width] + image->B[x + y * width]) / 3;
                SetPixelR(image, x, y, average);
                SetPixelG(image, x, y, average);
                SetPixelB(image, x, y, average);
            }
        }
    }
    if (PRINT)
    {
        printf("\"Black & White\" operation is done!\n");
    }
    return image;
} /*end of BlackNWhite*/

IMAGE
*VFlip(IMAGE *org_image)
{
    omp_set_dynamic(0);     // Explicitly disable dynamic teams
    omp_set_num_threads(NUM_THREADS); // Use 4 threads for all consecutive parallel regions
    const unsigned int width = org_image->Width;
    const unsigned int height = org_image->Height;
    const unsigned int half_height = height / 2;
    int id = omp_get_thread_num();
    int j = org_image->Height - 1;
    IMAGE *image = org_image;
#pragma omp parallel
    {
        for (int y = id; y < half_height; y += NUM_THREADS)
        {
            for (int x = 0; x < width; x += NUM_THREADS)
            {
                unsigned char temp = GetPixelR(image, x, y);
                SetPixelR(image, x, y, GetPixelR(image, x, j));
                SetPixelR(image, x, j, temp);
                temp = GetPixelG(image, x, y);
                SetPixelG(image, x, y, GetPixelG(image, x, j));
                SetPixelG(image, x, j, temp);
                temp = GetPixelB(image, x, y);
                SetPixelB(image, x, y, GetPixelB(image, x, j));
                SetPixelB(image, x, j, temp);
            }
            --j;
        }
    }
    if (PRINT)
    {
        printf("\"VFlip\" operation is done!\n");
    }
    return image;
} /*end of VFlip*/

IMAGE
*HMirror(IMAGE *org_image)
{
    omp_set_dynamic(0);     // Explicitly disable dynamic teams
    omp_set_num_threads(NUM_THREADS); // Use 4 threads for all consecutive parallel regions
    const unsigned int width = org_image->Width;
    const unsigned int height = org_image->Height;
    const unsigned int half_width = width / 2;
    int id = omp_get_thread_num();
    int i = org_image->Width - 1;
    IMAGE *image = org_image;

#pragma omp parallel
    {
        for (int y = id; y < height; y += NUM_THREADS)
        {
            for (int x = 0; x < half_width; x+= NUM_THREADS, --i)
            {
                SetPixelR(image, x, y, GetPixelR(image, i, y));
                SetPixelG(image, x, y, GetPixelG(image, i, y));
                SetPixelB(image, x, y, GetPixelB(image, i, y));
            }
            i = width - 1;
        }
    }

    if (PRINT)
    {
        printf("\"HMirror\" operation is done!\n");
    }
    return image;
} /*end of HMirror*/

/*EOF DIPs.h*/
