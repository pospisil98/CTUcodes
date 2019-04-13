#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdint.h>
 
int main(int argc, char* argv[]) {
    FILE* imgfile = fopen(argv[1], "rb");
    int width, height, max_val;
     
    if(fscanf(imgfile, "P6\n%d\n%d\n%d\n", &width, &height, &max_val)){};
     
    uint8_t* pixels = (uint8_t*)malloc(width * height * 3 * sizeof(uint8_t));
    uint8_t* pixelsNew = (uint8_t*)malloc(width * height * 3 * sizeof(uint8_t));
     
    memset(pixelsNew, 0, width * height * 3 * sizeof(uint8_t));
     
    if(fread(pixels, 1, (width * height * 3), imgfile));
     
    int r, g, b, row, col, currentPos;
    int h0, h1, h2, h3, h4;
    h0 = h1 = h2 = h3 = h4 = 0;
    unsigned char greyscale = 0;
     
    // first line
    for(int i = 0; i < (width*3); i += 3) {
        pixelsNew[i] = pixels[i];
        pixelsNew[i+1] = pixels[i+1];
        pixelsNew[i+2] = pixels[i+2];
         
        greyscale = (unsigned char)(0.2126*pixelsNew[i] +
             0.7152*pixelsNew[i + 1] +
             0.0722*pixelsNew[i + 2] + 0.5);
                 
        if(greyscale >= 204) {
            h4++;
        } else if(greyscale >= 153) {
            h3++;
        } else if(greyscale >= 102) {
            h2++;
        } else if(greyscale >= 51) {
            h1++;
        } else {
            h0++;
        }
    }
     
    // last line
    for(int i = ((height - 1) * width * 3); i < (height * width * 3); i += 3) {
        pixelsNew[i] = pixels[i];
        pixelsNew[i+1] = pixels[i+1];
        pixelsNew[i+2] = pixels[i+2];
         
        greyscale = (unsigned char)(0.2126*pixelsNew[i] +
             0.7152*pixelsNew[i + 1] +
             0.0722*pixelsNew[i + 2] + 0.5);
                 
        if(greyscale >= 204) {
            h4++;
        } else if(greyscale >= 153) {
            h3++;
        } else if(greyscale >= 102) {
            h2++;
        } else if(greyscale >= 51) {
            h1++;
        } else {
            h0++;
        }
    }
     
    // first col
    for(int i = (width*3) ; i < ((height - 1) * (width) * 3); i += (width*3)) {
        pixelsNew[i] = pixels[i];
        pixelsNew[i+1] = pixels[i+1];
        pixelsNew[i+2] = pixels[i+2];
         
        greyscale = (unsigned char)(0.2126*pixelsNew[i] +
             0.7152*pixelsNew[i + 1] +
             0.0722*pixelsNew[i + 2] + 0.5);
                 
        if(greyscale >= 204) {
            h4++;
        } else if(greyscale >= 153) {
            h3++;
        } else if(greyscale >= 102) {
            h2++;
        } else if(greyscale >= 51) {
            h1++;
        } else {
            h0++;
        }
    }
     
    // last col
    for(int i = (2 * width * 3 - 3) ; i < ((height - 1) * width * 3); i += (width*3)) {
        pixelsNew[i] = pixels[i];
        pixelsNew[i+1] = pixels[i+1];
        pixelsNew[i+2] = pixels[i+2];
         
        greyscale = (unsigned char)(0.2126*pixelsNew[i] +
             0.7152*pixelsNew[i + 1] +
             0.0722*pixelsNew[i + 2] + 0.5);
                 
        if(greyscale >= 204) {
            h4++;
        } else if(greyscale >= 153) {
            h3++;
        } else if(greyscale >= 102) {
            h2++;
        } else if(greyscale >= 51) {
            h1++;
        } else {
            h0++;
        }
    }
     
    for(row = 1; row < (height - 1); row++) {
        for(col = 1; col < (width - 1); col++) {
            currentPos = (row * (width*3) + (col * 3));
             
 
            r = (-pixels[currentPos - (width * 3)]) - 
                (pixels[currentPos - 3]) +  
                (pixels[currentPos] * 5) - 
                (pixels[currentPos + 3]) - 
                (pixels[currentPos + (width * 3)]);
             
            currentPos++;
             
            g = (-pixels[currentPos - (width * 3)]) - 
                (pixels[currentPos - 3]) +  
                (pixels[currentPos] * 5) - 
                (pixels[currentPos + 3]) - 
                (pixels[currentPos + (width * 3)]);
             
            currentPos++;
             
            b = (-pixels[currentPos - (width * 3)]) - 
                (pixels[currentPos - 3]) +  
                (pixels[currentPos] * 5) - 
                (pixels[currentPos + 3]) - 
                (pixels[currentPos + (width * 3)]);
                 
            if(r > 255)
                r = 255;
            else if(r < 0)
                r = 0;
             
            if(g > 255)
                g = 255;
            else if(g < 0)
                g = 0;
             
            if(b > 255)
                b = 255;
            else if(b < 0)
                b = 0;
                 
            pixelsNew[currentPos] = b;
            pixelsNew[currentPos - 1] = g;
            pixelsNew[currentPos - 2] = r;
             
            greyscale = (unsigned char)(0.2126*r + 0.7152*g + 0.0722*b + 0.5);
                 
            if(greyscale >= 204) {
                h4++;
            } else if(greyscale >= 153) {
                h3++;
            } else if(greyscale >= 102) {
                h2++;
            } else if(greyscale >= 51) {
                h1++;
            } else {
                h0++;
            }
        }
    }
     
    fclose(imgfile);
     
    FILE* imgfilesave = fopen("output.ppm", "wb");
    fprintf(imgfilesave, "P6\n%d %d\n255\n", width, height);
    fwrite(pixelsNew, (width * height * 3), sizeof(char), imgfilesave);
    fclose(imgfilesave);    
     
    FILE* histogramfilesave = fopen("output.txt", "wb");
    fprintf(histogramfilesave, "%d %d %d %d %d ", h0, h1, h2, h3, h4);
    fclose(histogramfilesave);
             
    free(pixels);
    free(pixelsNew);
}