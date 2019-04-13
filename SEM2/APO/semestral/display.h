#ifndef DISPLAY_H
#define DISPLAY_H

#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

extern uint16_t glcd[320][480];

uint16_t getLcdColor(int r, int g, int b);

void gclear(unsigned char c);

void gset(unsigned char c);

void drawData(unsigned char *parlcd_mem_base, uint16_t* data);

void putCharacter(char c, int posX, int posY, uint16_t* data, uint16_t color);

void putWord(char* word, int posX, int posY, uint16_t* data, uint16_t color);

void drawJuliaInfo(double x, double y, double im, double re, uint16_t* data);

#ifdef __cplusplus
} /* extern "C" */
#endif
#endif
