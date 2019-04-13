#ifndef JULIA_H
#define JULIA_H

#ifdef __cplusplus
extern "C" {
#endif
// -------------------------------------------------

#include <stdint.h>

typedef struct RgbColor
{
    unsigned char r;
    unsigned char g;
    unsigned char b;
} RgbColor;

typedef struct HsvColor
{
    unsigned char h;
    unsigned char s;
    unsigned char v;
} HsvColor;

RgbColor hsvToRgb(HsvColor hsv);

void calculateJulia(double zoom, double moveX, double moveY, double cRe, double cIm, uint16_t* data); 
 
// -------------------------------------------------
#ifdef __cplusplus
} /* extern "C" */
#endif
#endif

