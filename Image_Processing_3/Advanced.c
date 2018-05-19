/*Advanced*/
#include "Advanced.h"

#ifndef DEBUG
    #define PRINT 0
#else
    #define PRINT 1
#endif

IMAGE *CallPosterize(IMAGE *image)
{
    unsigned int rbits, gbits, bbits;

    printf("Enter the number of posterization bits for R channel (1 to 8): ");
    scanf("%d", &rbits);
    printf("Enter the number of posterization bits for G channel (1 to 8): ");
    scanf("%d", &gbits);
    printf("Enter the number of posterization bits for B channel (1 to 8): ");
    scanf("%d", &bbits);

    return Posterize(image, rbits, gbits, bbits);
}

IMAGE *Posterize(IMAGE *image, unsigned char rbits, unsigned char gbits, unsigned char bbits)
{
    int x, y, width, height;

    width = image->Width;
    height = image->Height;

    for( y = 0; y < height; y++ )
    {
        for( x = 0; x < width; x++ )
        {
            SetPixelR(image, x, y, ChangeBit(GetPixelR(image, x, y), rbits));
            SetPixelG(image, x, y, ChangeBit(GetPixelG(image, x, y), rbits));
            SetPixelB(image, x, y, ChangeBit(GetPixelB(image, x, y), rbits));
        }
    }

    if (PRINT){
        printf("\"Posterize\" operation is done!\n");
    }
    return image;
}

unsigned char ChangeBit (unsigned char value, unsigned int modifier)
{
    int i;

    value = value >> modifier;
    value = value << 1;

    for (i = 1; i < modifier; i++)
    {
        value = value << 1;
        value = value | 1;
    }
    return value;
}

IMAGE
*CallAddNoise(IMAGE *image)
{
    int n;
    printf("Please input noise percentage: ");
    scanf("%d", &n);
    return AddNoise(n, image);
}
IMAGE
*AddNoise (int n, IMAGE *image)
{
    int i, x, y, width, height;

    width = image->Width;
    height = image->Height;

    srand(time(NULL));
    n = n * width * height /100;

    for(i = 0; i < n; i++)
    {
        x = rand() % width;
        y = rand() % height;
        SetPixelR(image, x, y, 255);
        SetPixelG(image, x, y, 255);
        SetPixelB(image, x, y, 255);
    }
    if (PRINT){
        printf("\"AddNoise\" operation is done!\n");
    }
    return image;
}

IMAGE
*CallOverlay (IMAGE *image)
{
    char fname [SLEN];
    unsigned int x_offset, y_offset;

    printf("Please input the file name for the second image: ");
    scanf("%s", fname);
    printf("Please input x coordinate of the overlay image: ");
    scanf("%d", &x_offset);
    printf("Please input y coordinate of the overlay image: ");
    scanf("%d", &y_offset);

    return Overlay(fname, image, x_offset, y_offset);
}
IMAGE
*Overlay  (char fname[SLEN], IMAGE *image, unsigned int x_offset, unsigned int y_offset)
{
    /*RGB Arrays for overlay image*/
    IMAGE *Oimage = NULL;
    int x, y;
    int width, height;

    width = image->Width;
    height = image->Height;

    Oimage = ReadImage(fname);

    for(y = y_offset; y < height && y-y_offset < Oimage->Height; y++)
    {
        for(x = x_offset; x < width && x-x_offset < Oimage->Width; x++)
        {
            if( !(GetPixelR(Oimage, x-x_offset,y-y_offset) >= 250 &&
                 (GetPixelG(Oimage, x-x_offset,y-y_offset) >= 250)&&
			     (GetPixelB(Oimage, x-x_offset,y-y_offset) >= 250)))
            {
                SetPixelR(image, x, y, GetPixelR(Oimage, x-x_offset, y-y_offset));
                SetPixelG(image, x, y, GetPixelG(Oimage, x-x_offset, y-y_offset));
                SetPixelB(image, x, y, GetPixelB(Oimage, x-x_offset, y-y_offset));
		    }
        }
    }

    if (PRINT){
        printf("\"Overlay\" operation is done!\n");
    }
    DeleteImage(Oimage);
    return image;
}

IMAGE
*Surprise (IMAGE *image)
{
    /*temp to store the modified pixel*/
    IMAGE *tempImage = NULL;
    unsigned int width, height;
    int x, y, temp, r; /*r is the modifier*/
    width = image->Width;
    height = image->Height;

    r = 10;
    tempImage = CreateImage(width, height);

    /*Make Copy*/
    for ( y = 1; y < height; y++ )
    {
        for ( x = 1; x < width; x++ )
        {
            SetPixelR(tempImage, x, y, GetPixelR(image, x, y));
            SetPixelG(tempImage, x, y, GetPixelG(image, x, y));
            SetPixelB(tempImage, x, y, GetPixelB(image, x, y));
        }
    }
    x = width- 1;
    y = height -1;
    /*Four corners, each pixel has only 3 neighbors*/
    /*For R*/
    temp = r*GetPixelR(image, 0, 0) - GetPixelR(image, 0, 1) - GetPixelR(image, 1, 0) - GetPixelR(image, 1, 1);
    SetPixelR(tempImage, 0, 0, Inrange(temp));
    temp = r*GetPixelR(image, x, 0) - GetPixelR(image, x-1, 0) - GetPixelR(image, x-1, 1) - GetPixelR(image, x, 1);
    SetPixelR(tempImage, x, 0, Inrange(temp));
    temp = r*GetPixelR(image, 0, y) - GetPixelR(image, 1, y) - GetPixelR(image, 1, y-1) - GetPixelR(image, 0, y);
    SetPixelR(tempImage, 0, y, Inrange(temp));
    temp = r*GetPixelR(image, x, y) - GetPixelR(image, x, y-1)- GetPixelR(image, x-1, y-1) - GetPixelR(image, x-1, y);
    SetPixelR(tempImage, x, y, Inrange(temp));
    /*For G*/
    temp = r*GetPixelG(image, 0, 0) - GetPixelG(image, 0, 1) - GetPixelG(image, 1, 0) - GetPixelG(image, 1, 1);
    SetPixelG(tempImage, 0, 0, Inrange(temp));
    temp = r*GetPixelG(image, x, 0) - GetPixelG(image, x-1, 0) - GetPixelG(image, x-1, 1) - GetPixelG(image, x, 1);
    SetPixelG(tempImage, x, 0, Inrange(temp));
    temp = r*GetPixelG(image, 0, y) - GetPixelG(image, 1, y) - GetPixelG(image, 1, y-1) - GetPixelG(image, 0, y);
    SetPixelG(tempImage, 0, y, Inrange(temp));
    temp = r*GetPixelG(image, x, y) - GetPixelG(image, x, y-1)- GetPixelG(image, x-1, y-1) - GetPixelG(image, x-1, y);
    SetPixelG(tempImage, x, y, Inrange(temp));
    /*For B*/
    temp = r*GetPixelB(image, 0, 0) - GetPixelB(image, 0, 1) - GetPixelB(image, 1, 0) - GetPixelB(image, 1, 1);
    SetPixelB(tempImage, 0, 0, Inrange(temp));
    temp = r*GetPixelB(image, x, 0) - GetPixelB(image, x-1, 0) - GetPixelB(image, x-1, 1) - GetPixelB(image, x, 1);
    SetPixelB(tempImage, x, 0, Inrange(temp));
    temp = r*GetPixelB(image, 0, y) - GetPixelB(image, 1, y) - GetPixelB(image, 1, y-1) - GetPixelB(image, 0, y);
    SetPixelB(tempImage, 0, y, Inrange(temp));
    temp = r*GetPixelB(image, x, y) - GetPixelB(image, x, y-1)- GetPixelB(image, x-1, y-1) - GetPixelB(image, x-1, y);
    SetPixelB(tempImage, x, y, Inrange(temp));
    /*End of modifying 4 corner*/

    /*For 4 edges, each pixel has only 5 neighbor*/
    for (x = 0, y = 1; y < height-1; y++)/*upper edge*/
    {
        temp = r*GetPixelR(image, x, y)   - GetPixelR(image, x, y-1)   - GetPixelR(image, x+1, y-1)
               - GetPixelR(image, x+1, y) - GetPixelR(image, x+1, y+1) - GetPixelR(image, x, y+1);
        SetPixelR(tempImage, x, y, Inrange(temp));
        temp = r*GetPixelG(image, x, y)   - GetPixelG(image, x, y-1)   - GetPixelG(image, x+1, y-1)
               - GetPixelG(image, x+1, y) - GetPixelG(image, x+1, y+1) - GetPixelG(image, x, y+1);
        SetPixelG(tempImage, x, y, Inrange(temp));
        temp = r*GetPixelB(image, x, y)   - GetPixelB(image, x, y-1)   - GetPixelB(image, x+1, y-1)
               - GetPixelB(image, x+1, y) - GetPixelB(image, x+1, y+1) - GetPixelB(image, x, y+1);
        SetPixelB(tempImage, x, y, Inrange(temp));

    }
    for (x = width-1, y = 1; y < height-1; y++)/*lower edge*/
    {
        temp = r*GetPixelR(image, x, y)  - GetPixelR(image, x, y-1)   - GetPixelR(image, x-1, y-1)
               - GetPixelR(image,x-1, y) - GetPixelR(image, x-1, y+1) - GetPixelR(image, x, y+1);
        SetPixelR(tempImage, x, y, Inrange(temp));
        temp = r*GetPixelG(image, x, y)  - GetPixelG(image, x, y-1)   - GetPixelG(image, x-1, y-1)
               - GetPixelG(image,x-1, y) - GetPixelG(image, x-1, y+1) - GetPixelG(image, x, y+1);
        SetPixelG(tempImage, x, y, Inrange(temp));
        temp = r*GetPixelB(image, x, y)  - GetPixelB(image, x, y-1)   - GetPixelB(image, x-1, y-1)
               - GetPixelB(image,x-1, y) - GetPixelB(image, x-1, y+1) - GetPixelB(image, x, y+1);
        SetPixelB(tempImage, x, y, Inrange(temp));
    }
    for (x = 1, y = 0; x < width-1; x++)/*left edge*/
    {
        temp = r*GetPixelR(image, x, y)   - GetPixelR(image, x-1, y)   - GetPixelR(image, x-1, y+1)
               - GetPixelR(image, x, y+1) - GetPixelR(image, x+1, y+1) - GetPixelR(image, x+1, y);
        SetPixelR(tempImage, x, y, Inrange(temp));
        temp = r*GetPixelG(image, x, y)   - GetPixelG(image, x-1, y)   - GetPixelG(image, x-1, y+1)
               - GetPixelG(image, x, y+1) - GetPixelG(image, x+1, y+1) - GetPixelG(image, x+1, y);
        SetPixelG(tempImage, x, y, Inrange(temp));
        temp = r*GetPixelB(image, x, y)   - GetPixelB(image, x-1, y)   - GetPixelB(image, x-1, y+1)
               - GetPixelB(image, x, y+1) - GetPixelB(image, x+1, y+1) - GetPixelB(image, x+1, y);
        SetPixelB(tempImage, x, y, Inrange(temp));

    }
    for (x = 1, y = height-1; x < width-1; x++)/*right edge*/
    {
        temp = r*GetPixelR(image, x, y)   - GetPixelR(image, x-1, y)   - GetPixelR(image, x-1, y-1)
               - GetPixelR(image, x, y-1) - GetPixelR(image, x+1, y-1) - GetPixelR(image, x+1, y);
        SetPixelR(tempImage, x, y, Inrange(temp));
        temp = r*GetPixelG(image, x, y)   - GetPixelG(image, x-1, y)   - GetPixelG(image, x-1, y-1)
               - GetPixelG(image, x, y-1) - GetPixelG(image, x+1, y-1) - GetPixelG(image, x+1, y);
        SetPixelG(tempImage, x, y, Inrange(temp));
        temp = r*GetPixelB(image, x, y)   - GetPixelB(image, x-1, y)   - GetPixelB(image, x-1, y-1)
               - GetPixelB(image, x, y-1) - GetPixelB(image, x+1, y-1) - GetPixelB(image, x+1, y);
        SetPixelB(tempImage, x, y, Inrange(temp));
    }
    /*End of modifying 4 edges*/
    /*For the inner image, without the 4 edges and 4 corners*/
    for ( y = 1; y < height-1; y++ )
    {
        for ( x = 1; x < width-1; x++ )
        {
            temp                = r*image->R[x+y*width]       - image->R[x-1+y*width]     - image->R[x-1+(y-1)*width]
                                  - image->R[x+(y-1)*width]   - image->R[x+1+(y-1)*width] - image->R[x+1+y*width]
                                  - image->R[x+1+(y+1)*width] - image->R[x+(y+1)*width]   - image->R[x-1+(y+1)*width];
            tempImage->R[x+y*width] = Inrange(temp);
            temp                = r*image->G[x+y*width]       - image->G[x-1+y*width]     - image->G[x-1+(y-1)*width]
                                  - image->G[x+(y-1)*width]   - image->G[x+1+(y-1)*width] - image->G[x+1+y*width]
                                  - image->G[x+1+(y+1)*width] - image->G[x+(y+1)*width]   - image->G[x-1+(y+1)*width];
            tempImage->G[x+y*width] = Inrange(temp);
            temp                = r*image->B[x+y*width]       - image->B[x-1+y*width]     - image->B[x-1+(y-1)*width]
                                  - image->B[x+(y-1)*width]   - image->B[x+1+(y-1)*width] - image->B[x+1+y*width]
                                  - image->B[x+1+(y+1)*width] - image->B[x+(y+1)*width]   - image->B[x-1+(y+1)*width];
            tempImage->B[x+y*width] = Inrange(temp);
        }
    }
    /*end of modifying the inner image*/

    for ( y = 0; y < height; y++ )
    {
        for ( x = 0; x < width; x++ )
        {
            SetPixelB(tempImage, x, y, ( GetPixelR(tempImage, x, y)+GetPixelG(tempImage, x, y)+GetPixelB(tempImage, x, y) )/5);
            SetPixelR(tempImage, x, y, (unsigned char) GetPixelB(tempImage, x, y)*1.6);
            SetPixelG(tempImage, x, y, (unsigned char) GetPixelB(tempImage, x, y)*1.6);
        }
    }

    if (PRINT){
        printf("\"Surprise\" operation is done!\n");
    }
    DeleteImage(image);
    return tempImage;
}

IMAGE *CallResize (IMAGE *image)
{
    int input;
    printf("Please input the resizing percentage (integer between 1˜500): ");
    scanf("%d", &input);
    return Resize(input, image);
}
IMAGE *Resize( unsigned int percentage, IMAGE *image)
{
    IMAGE *resized = NULL;
    int x, y, width, height, xold, yold;
    int tempR, tempG, tempB;

    width = image->Width * percentage/100.0;
    height = image->Height * percentage/100.0;
    tempR = 0;
    tempG = 0;
    tempB = 0;

    if (percentage ==100){
        return image;
    }

    resized = CreateImage(width, height);
    if (percentage > 100){
        for (y = 0; y < height; y++){
            for (x = 0; x < width; x++){
                xold = x/(percentage/100.0);
                yold = y/(percentage/100.0);
                SetPixelR(resized, x, y, GetPixelR(image, xold, yold));
                SetPixelG(resized, x, y, GetPixelG(image, xold, yold));
                SetPixelB(resized, x, y, GetPixelB(image, xold, yold));
            }
        }
    }
    else{
        for (y = 0; y < height; y++){
            for (x = 0; x < width; x++){
                for(xold = x/(percentage/100.0); (xold*(percentage/100.0)) <= x; xold++){
                    for(yold = y/(percentage/100.0); (yold*(percentage/100.0)) <= y; yold++){
                        tempR += GetPixelR(image, xold, yold);
                        tempG += GetPixelG(image, xold, yold);
                        tempB += GetPixelB(image, xold, yold);
                    }
                }
                SetPixelR(resized, x, y, tempR);
                SetPixelG(resized, x, y, tempG);
                SetPixelB(resized, x, y, tempB);
                tempR = 0;
                tempG = 0;
                tempB = 0;
            }
        }
    }
    if (PRINT){
        printf("\"Resized\" operation is done!\n");
    }
    DeleteImage(image);
    return resized;
}

IMAGE *Rotate(IMAGE *image)
{
    int x, y, width, height;
    width = image->Width;
    height = image->Height;
    IMAGE *rotated = NULL;
    rotated = CreateImage(height, width);

    for (y = 0; y < height; y++)
    {
        for (x = 0; x < width; x++)
        {
            SetPixelR(rotated,height-y-1, x, GetPixelR(image, x, y));
            SetPixelG(rotated,height-y-1, x, GetPixelG(image, x, y));
            SetPixelB(rotated,height-y-1, x, GetPixelB(image, x, y));
        }
    }
    if (PRINT){
        printf("\"Rotate\" operation is done!\n");
    }
    DeleteImage(image);
    return rotated;
}

IMAGE *CallJuliaset (void)
{
    unsigned int W, H, max_interation;
    printf("Please input the width of the Julia set image: ");
    scanf("%d", &W);
    printf("Please input the height of the Julia set image: ");
    scanf("%d", &H);
    printf("Please input the max iteration for the Julia set calculation: ");
    scanf("%d", &max_interation);

    return Juliaset(W, H, max_interation);
}

IMAGE *Juliaset(unsigned int W, unsigned int H, unsigned int max_iteration)
{
    const unsigned char palette[MAX_COLOR][3] = {
    /* r g b*/
    {   0,   0,   0 }, /* 0, black */
    { 127,   0,   0 }, /* 1, brown */
    { 255,   0,   0 }, /* 2, red */
    { 255, 127,   0 }, /* 3, orange */
    { 255, 255,   0 }, /* 4, yellow */
    { 127, 255,   0 }, /* 5, light green */
    {   0, 255,   0 }, /* 6, green */
    {   0, 255, 127 }, /* 7, blue green */
    {   0, 255, 255 }, /* 8, turquoise */
    { 127, 255, 255 }, /* 9, light blue */
    { 255, 255, 255 }, /* 10, white */
    { 255, 127, 255 }, /* 11, pink */
    { 255,   0, 255 }, /* 12, light pink */
    { 127,   0, 255 }, /* 13, purple */
    {   0,   0, 255 }, /* 14, blue */
    {   0,   0, 127 }  /* 15, dark blue */
    };
    int x, y, i;
    IMAGE *image = NULL;
    image = CreateImage(W, H);

    /*The following code is taken (with very few adaptation) from:*/
    /*http://lodev.org/cgtutor/juliamandelbrot.html*/

    /*each iteration, it calculates: new = old*old + c, where c is a constant and old starts at current pixel*/
    double cRe, cIm;                   /*real and imaginary part of the constant c, determinate shape of the Julia Set*/
    double newRe, newIm, oldRe, oldIm;   /*real and imaginary parts of new and old*/
    double zoom = 1, moveX = 0, moveY = 0; /*you can change these to zoom and change position*/

    /*int max_iteration is past to this function*/

    /*pick some values for the constant c, this determines the shape of the Julia Set*/
    cRe = -0.7;
    cIm = 0.27015;

    /*loop through every pixel*/
    for(x = 0; x < W; x++){
        for(y = 0; y < H; y++)
        {
            /*calculate the initial real and imaginary part of z, based on the pixel location and zoom and position values*/
            newRe = 1.5 * (x - W / 2.0) / (0.5 * zoom * W) + moveX;
            newIm = (y - H / 2.0) / (0.5 * zoom * H) + moveY;
            /*i will represent the number of iterations*/
            /*start the iteration process*/
            for(i = 0; i < max_iteration; i++)
            {
                /*remember value of previous iteration*/
                oldRe = newRe;
                oldIm = newIm;
                /*the actual iteration, the real and imaginary part are calculated*/
                newRe = oldRe * oldRe - oldIm * oldIm + cRe;
                newIm = 2 * oldRe * oldIm + cIm;
                /*if the point is outside the circle with radius 2: stop*/
                if((newRe * newRe + newIm * newIm) > 4) break;
            }

            /*use color model conversion to get rainbow palette, make brightness black if maxIterations reached*/
            SetPixelR(image, x, y, palette[i % 16][0]);
            SetPixelG(image, x, y, palette[i % 16][1]);
            SetPixelB(image, x, y, palette[i % 16][2]);
        }
    }
    if (PRINT){
        printf("\"Julia Set\" operation is done!\n");
    }
    return image;
}

IMAGE *CallCrop (IMAGE *image)
{
    assert(image);

    unsigned int x, y, w, h;
    printf("Please enter the X offset value: ");
    scanf("%d", &x);
    printf("Please enter the Y offset value: ");
    scanf("%d", &y);
    printf("Please input the crop width: ");
    scanf("%d", &w);
    printf("Please input the crop height: ");
    scanf("%d", &h);
    return Crop(image, x, y, w, h);
}

IMAGE *Crop(IMAGE *image, unsigned int x, unsigned int y, unsigned int W, unsigned int H)
{
    assert(W < (image->Width - x));
    assert(H < (image->Height -y));

    IMAGE *croped = NULL;
    int i, j;

    croped = CreateImage(W, H);
    for(i = 0; i < W; i++){
        for(j = 0; j < H; j++){
            SetPixelR(croped, i, j, GetPixelR(image, x+i, y+j));
            SetPixelG(croped, i, j, GetPixelG(image, x+i, y+j));
            SetPixelB(croped, i, j, GetPixelB(image, x+i, y+j));
        }
    }
    if(PRINT){
        printf("\"Crop\" operation is done!\n");
    }
    DeleteImage(image);
    return croped;
}

/*EOF Advanced.c*/
