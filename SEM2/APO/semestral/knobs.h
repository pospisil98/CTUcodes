#ifndef KNOBS_H
#define KNOBS_H

#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

int getRelativeValue(int current);

void getKnobs(int* rK, int* gK, int* bK, int* rB, int* gB, int* bB, unsigned char *mem_base);

#ifdef __cplusplus
} /* extern "C" */
#endif
#endif

