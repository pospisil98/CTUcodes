#define _POSIX_C_SOURCE 200112L

#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <time.h>
#include <unistd.h>
#include <string.h>

#include "mzapo_parlcd.h"
#include "mzapo_phys.h"
#include "mzapo_regs.h"
#include "font_types.h"

uint16_t glcd[320][480];
extern unsigned char *parlcd_mem_base;
int w = 480;
int h = 320;

uint16_t getLcdColor(int r, int g, int b) {
	return ((r << 11) & 0xF800) | ((g << 5) & 0x07E0) | (b & 0x001F);
}

void gclear(unsigned char c) {
    int i;
    
    for(i = 0; i < 320; i++) {
        memset(glcd[i], c, 480 * sizeof(uint16_t));
    }
}

void drawData(unsigned char *parlcd_mem_base, uint16_t* data) {
	for(int y = 0; y < h; y++) {
		for(int x = 0; x < w; x++) {
			parlcd_write_data(parlcd_mem_base, data[y*w+x]);
		}
	}
}

void putCharacter(char c, int posX, int posY, uint16_t* data, uint16_t color) {
	for (int y = 0; y < 16; y++) {
        for (int x = 0; x < 8; x++) {
            if (font_rom8x16.bits[c * 16 + y] >> (15 - x) & 1) {
                data[(posX * 8 + x) + (y + posY * 16) * 480] = color;
            }
        }
	}
}

void putWord(char* word, int posX, int posY, uint16_t* data, uint16_t color) {
	char c;
	int i = 0;
	while((c = word[i]) != '\0') {
		putCharacter(c, posX + i, posY, data, color);
		i++;
	}
}

void drawJuliaInfo(double x, double y, double im, double re, uint16_t* data) {
	char string[10];
	uint16_t color = getLcdColor(255,255,255);
	sprintf(string, "x=%f", (-1 * x));
	putWord(string, 0, 0, data, color);
	sprintf(string, "y=%f", y);
	putWord(string, 0, 1, data, color);
	sprintf(string, "Im=%f", im);
	putWord(string, 0, 2, data, color);
	sprintf(string, "Re=%f", re);
	putWord(string, 0, 3, data, color);
}
