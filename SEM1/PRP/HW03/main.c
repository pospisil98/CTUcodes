#include <stdio.h>
#include <stdlib.h>


void makeRoof(int width);
void makeFullLine(int width, int isCeiling, int isFence, int fenceSize);
void makeWalls(int width, int height, int isFence, int fenceSize);
void makeFenceFullLine(int width);
void makeFenceEmptyLine(int width);


/* The main program */
int main(int argc, char *argv[])
{
	int width = 0;
	int height = 0;
	int fenceSize = 0;
	int isFence = 0;
	int isCeiling = 1;
	int numbersReadFence = 0;
	
	int numbersRead = scanf("%d %d", &width, &height);
	
	if(numbersRead != 2){
		fprintf(stderr, "%s", "Error: Chybny vstup!\n");
		return 100;
	} else if(width > 69 || width < 3 || height > 69 || height < 3) {
		fprintf(stderr, "%s", "Error: Vstup mimo interval!\n");
		return 101;
	} else if(width % 2 == 0) {
		fprintf(stderr, "%s", "Error: Sirka neni liche cislo!\n");
		return 102;
	} else if(width == height) {
		numbersReadFence = scanf("%d", &fenceSize);
		
		if(numbersReadFence != 1){
			fprintf(stderr, "%s", "Error: Chybny vstup!\n");
			return 100;
		} else if(fenceSize > 69 || fenceSize < 3 || fenceSize >= height) {
			fprintf(stderr, "%s", "Error: Neplatna velikost plotu!\n");
			return 103;
		} else {
			isFence = 1;
		}
	} 
	
	makeRoof(width);
	makeFullLine(width, isCeiling, isFence, fenceSize);
	isCeiling = 0;
	makeWalls(width, height, isFence, fenceSize);
	makeFullLine(width, isCeiling, isFence, fenceSize);
	
	return 0;
}

void makeRoof(int width){
	int spaceCountSide = width / 2;
	int spaceCountMiddle = -1;
	
	while(spaceCountSide != 0) {
		for(int i = 0; i < spaceCountSide;i++){
			printf(" ");
		}
	
		printf("X");
	
		if(spaceCountMiddle != -1) {
			for(int i = 0; i < spaceCountMiddle;i++){
				printf(" ");
			}
			printf("X");
		}
	
		printf("\n");
		
		spaceCountSide--;
		spaceCountMiddle += 2;
	}	
}

void makeFullLine(int width, int isCeiling, int isFence, int fenceSize){
	for(int i = 0; i < width;i++){
		printf("X");
	}
	
	if(isFence == 1 && isCeiling == 0){
		makeFenceFullLine(fenceSize);
	} else {
		printf("\n");
	} 
}

void makeWalls(int width, int height, int isFence, int fenceSize){
	int spaceCountMiddle = width - 2;
	int fenceStart = height - (height - fenceSize);

	//cycle hrough lines
	for(int i = height - 1; i > 1; i--){
		//cycle through characters
		printf("X");
		for(int j = 0; j < spaceCountMiddle; j++){
			if(isFence == 1) {	
				if(i % 2 == 0){	
					if(j % 2 == 0) {
						printf("o");
					} else {
						printf("*");
					}
				} else {
					if(j % 2 == 0) {
						printf("*");
					} else {
						printf("o");
					}
				}
			} else {
				printf(" ");
			}
		}
		printf("X");
		
		if(isFence == 0 || i > fenceStart){
			printf("\n");
		} else {
			 if (i == fenceStart) {
			 	makeFenceFullLine(fenceSize);
			 } else if (i < fenceStart && i != height) {
			 	makeFenceEmptyLine(fenceSize);
			 } else if (i == 0) {
			 	makeFenceFullLine(fenceSize);
			 }
		}
	}
}

void makeFenceFullLine(int width){
	if(width % 2 == 1){
		printf("|");
		width--;
	}
	
	for(int i = width; i > 0; i--){
		if(i % 2 == 0) {
			printf("-");
		} else {
			printf("|");
		}
	}
	printf("\n");
}

void makeFenceEmptyLine(int width){
	for(int i = width; i > 0; i--){
	if(i % 2 == 0) {
			printf(" ");
		} else {
			printf("|");
		}
	}
	printf("\n");
}
