#define _POSIX_C_SOURCE 200112L

#include <stdlib.h>
#include <math.h>

#include "julia.h"
#include "display.h"
#include "mzapo_parlcd.h"

RgbColor hsvToRgb(HsvColor hsv)
{
    RgbColor rgb;
    unsigned char region, remainder, p, q, t;

    if (hsv.s == 0)
    {
        rgb.r = hsv.v;
        rgb.g = hsv.v;
        rgb.b = hsv.v;
        return rgb;
    }

    region = hsv.h / 43;
    remainder = (hsv.h - (region * 43)) * 6; 

    p = (hsv.v * (255 - hsv.s)) >> 8;
    q = (hsv.v * (255 - ((hsv.s * remainder) >> 8))) >> 8;
    t = (hsv.v * (255 - ((hsv.s * (255 - remainder)) >> 8))) >> 8;

    switch (region)
    {
        case 0:
            rgb.r = hsv.v; rgb.g = t; rgb.b = p;
            break;
        case 1:
            rgb.r = q; rgb.g = hsv.v; rgb.b = p;
            break;
        case 2:
            rgb.r = p; rgb.g = hsv.v; rgb.b = t;
            break;
        case 3:
            rgb.r = p; rgb.g = q; rgb.b = hsv.v;
            break;
        case 4:
            rgb.r = t; rgb.g = p; rgb.b = hsv.v;
            break;
        default:
            rgb.r = hsv.v; rgb.g = p; rgb.b = q;
            break;
    }
    return rgb;
}

void calculateJulia(double zoom, double xVal, double yVal, double cRe, double cIm, uint16_t* data) {
	int w = 480;
	int h = 320;  

	double newRe, newIm, oldRe, oldIm;
	RgbColor rcolor;
	HsvColor hcolor;
	int iter = 300;

	for(int y = 0; y < h; y++) {
		for(int x = 0; x < w; x++) {
			newRe = 1.5 * (x - w / 2) / (0.5 * zoom * w) + xVal;
			newIm = (y - h / 2) / (0.5 * zoom * h) + yVal;
			int i;
			for(i = 0; i < iter; i++) {
				oldRe = newRe;
				oldIm = newIm;
				
				newRe = oldRe * oldRe - oldIm * oldIm + cRe;
				newIm = 2 * oldRe * oldIm + cIm;
				
				if((newRe * newRe + newIm * newIm) > 4) {
					break;
				}
			}

			hcolor.h = i % 256;
			hcolor.s = 255;
			hcolor.v = 255 * (i < iter);
			rcolor = hsvToRgb(hcolor);

			data[y*w+x] = getLcdColor(rcolor.r, rcolor.g, rcolor.b);
		}
	}
}
