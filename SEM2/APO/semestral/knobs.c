#define _POSIX_C_SOURCE 200112L

#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <unistd.h>
#include <string.h>

#include "mzapo_parlcd.h"
#include "mzapo_phys.h"
#include "mzapo_regs.h"


float getRelativeValue(int current) {
	if (current > 128) {
		return -1 * (((256 - current) >> 3));
	} else {
		return current >> 2;
	}
}

void getKnobs(int* rK, int* gK, int* bK, int* rB, int* gB, int* bB, unsigned char *mem_base) {
	uint32_t rgb_knobs_value = *(volatile uint32_t*)(mem_base + SPILED_REG_KNOBS_8BIT_o);
	
	*rK = (rgb_knobs_value>>16) & 0xFF;	// red knob position
	*gK = (rgb_knobs_value>>8)  & 0xFF; // green knob position
	*bK = rgb_knobs_value      & 0xFF; 	// blue knob position
	
	*rB = (rgb_knobs_value>>26) & 1;    // red buttom
	*gB = (rgb_knobs_value>>25) & 1;    // green button
	*bB = (rgb_knobs_value>>24) & 1;    // blue button
}
