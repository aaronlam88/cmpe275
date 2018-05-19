#include "Image.h"

/* Get the color intensity of the Red channel of pixel (x, y) in image */
unsigned char GetPixelR(IMAGE *image, unsigned int x,  unsigned int y){
    return image->R[x+y*image->Width];
}

/* Get the color intensity of the Green channel of pixel (x, y) in image */
unsigned char GetPixelG(IMAGE *image, unsigned int x,  unsigned int y){
    return image->G[x+y*image->Width];
}

/* Get the color intensity of the Blue channel of pixel (x, y) in image */
unsigned char GetPixelB(IMAGE *image, unsigned int x,  unsigned int y){
    return image->B[x+y*image->Width];
}

/* Set the color intensity of the Red channel of pixel (x, y) in image with value r */
void SetPixelR(IMAGE *image, unsigned int x,  unsigned int y, unsigned char r){
    image->R[x+y*image->Width] = r;
    return;
}

/* Set the color intensity of the Green channel of pixel (x, y) in image with value g */
void SetPixelG(IMAGE *image, unsigned int x,  unsigned int y, unsigned char g){
    image->G[x+y*image->Width] = g;
    return;
}

/* Set the color intensity of the Blue channel of pixel (x, y) in image with value b */
void SetPixelB(IMAGE *image, unsigned int x,  unsigned int y, unsigned char b){
    image->B[x+y*image->Width] = b;
    return;
}

/* allocate the memory space for the image structure         */
/* and the memory spaces for the color intensity values.     */
/* return the pointer to the image, or NULL in case of error */
IMAGE *CreateImage(unsigned int Width, unsigned int Height){

    IMAGE *image = NULL;
    image = malloc(sizeof(IMAGE));
    image->Width = Width;
    image->Height = Height;
    image->R = malloc(sizeof(unsigned char)*Width*Height);
    image->G = malloc(sizeof(unsigned char)*Width*Height);
    image->B = malloc(sizeof(unsigned char)*Width*Height);
    if(image->R == NULL || image->B == NULL || image->G == NULL){
        return NULL;
    }

    return image;
}

/* release the memory spaces for the pixel color intensity values */
/* deallocate all the memory spaces for the image                 */
void DeleteImage(IMAGE *image){
    assert(image);
    free(image->R);
    free(image->G);
    free(image->B);
    free(image);

    return;
}
