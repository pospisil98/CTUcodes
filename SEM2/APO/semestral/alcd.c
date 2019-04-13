#define _POSIX_C_SOURCE 200112L

#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <time.h>
#include <unistd.h>

#include "mzapo_parlcd.h"
#include "mzapo_phys.h"
#include "mzapo_regs.h"
#include "display.h"
#include "julia.h"
#include "knobs.h"

unsigned char *parlcd_mem_base;
unsigned char *mem_base;

int main(int argc, char *argv[])
{		
		// lcd init
        unsigned char *parlcd_mem_base;    
        parlcd_mem_base = map_phys_address(PARLCD_REG_BASE_PHYS, PARLCD_REG_SIZE, 0);
        
        if (parlcd_mem_base == NULL)
			exit(1);
			
        parlcd_hx8357_init(parlcd_mem_base);
        parlcd_write_cmd(parlcd_mem_base, 0x2c);
		
		// knobs init
		unsigned char *mem_base;
		mem_base = map_phys_address(SPILED_REG_BASE_PHYS, SPILED_REG_SIZE, 0);	
		
		if (mem_base == NULL)
			exit(1);
		
        // data init
        int w = 480;
		int h = 320;
		int rK, gK, bK, rB, gB, bB;
		rK = gK = bK = rB = gB = bB = 0;
        double zoom = 1;
        double moveX = 0;
        double moveY = 0;
        double cRe = -0.7;
        double cIm = 0.27015;
        uint16_t* data = (uint16_t*)malloc(w*h*sizeof(uint16_t));
        
        // show init
        int indexShow = 0;
        double reShow[6] = {-0.7, -0.4, -0.8, 0.8, -0.7269, 0.3};
		double imShow[6] = {0.27123, 0.6, 0.156, 0.6, 0.1889, 0.6};
		
		while(rB != 1) {
			getKnobs(&rK, &gK, &bK, &rB, &gB, &bB, mem_base);			
			
			moveX = -1 * getRelativeValue(rK);
			moveY = getRelativeValue(gK);
			cIm = 0.27123 + (float)((float)getRelativeValue(bK) / 200);
			
			calculateJulia(zoom, moveX, moveY, cRe, cIm, data);
			drawJuliaInfo(moveX, moveY, cRe, cIm, data);
			drawData(parlcd_mem_base, data);
			
			// show mode part
			if(bB == 1) {
				putWord("SHOW MODE", 0, 4, data, getLcdColor(0,0,0));
				while(1) {
					getKnobs(&rK, &gK, &bK, &rB, &gB, &bB, mem_base);	
					
					calculateJulia(1, 0, 0, reShow[indexShow], imShow[indexShow], data);
					drawJuliaInfo(0, 0, reShow[indexShow], imShow[indexShow], data);
					putWord("SHOW MODE", 0, 5, data, getLcdColor(0,0,0));
					drawData(parlcd_mem_base, data);
					indexShow++;
					
					sleep(2);
					
					if(indexShow == 6)
						indexShow = 0;
						
					if(gB == 1)
						break;
				}
			}
		}
		
        return 0;
}
