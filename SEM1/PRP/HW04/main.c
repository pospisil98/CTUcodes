#include <stdio.h>
#include <stdlib.h>
#include <math.h>

void primes(long long int input, int *primesArray, int arrayLenght);
void erathostenes(int *arr, int count);

/* The main program */
int main(int argc, char *argv[])
{
	int primesArray[78500] = {0};
	int arrayLenght = sizeof(primesArray)/sizeof(int);
	
	erathostenes(primesArray, 1000000);
	
	int numScanned = 0;
	long long int i;
	     
    while((numScanned = scanf("%lld",&i))){
    	if(i < 0) {
    		fprintf(stderr, "%s", "Error: Chybny vstup!\n");
			return 100;
    	} else if (i == 0) {
    		return 0;
    	} else {
    		primes(i, primesArray, arrayLenght);
    	}
    }
    
    if(numScanned == 0) {
    	fprintf(stderr, "%s", "Error: Chybny vstup!\n");
		return 100;
    } else {
    	return 0;
    }
}

void erathostenes(int *arr, int count) {
    int numbers[count];
   
    /*fill the array with natural numbers*/
    for (int i = 0; i < count; i++){
    	//2 is here because its the first prime and we need to start from here
        numbers[i]=i+2;
    }

    /*sieve the non-primes*/
    for (int i = 0; i < count; i++){
        if (numbers[i] != -1){
            for (int j = 2 * numbers[i] - 2; j < count; j += numbers[i])
                numbers[j] = -1;
        }
    }

    /*transfer the primes to their own array*/
    int j = 0;
    for (int i = 0; i < count && j < count;i++)
        if (numbers[i] != -1)
            arr[j++] = numbers[i];
}

void primes(long long int input, int *primesArray, int arrayLenght){	
	printf("Prvociselny rozklad cisla %lld je:\n", input);
	
	if(input == 1){
		printf("1");
	} else {
		int dividerCount = 0;
		for(int i = 0; i < arrayLenght; i++) {
			if(primesArray[i] == 0) {
				continue;
			}
			
			if(primesArray[i] > input) { 
				break;
			}

			while(input % primesArray[i] == 0) {
				input = input / primesArray[i];
				dividerCount++;
			}
			
			if(dividerCount > 1) {
				printf("%d^%d", primesArray[i], dividerCount);
			} else if (dividerCount == 1) {
				printf("%d", primesArray[i]);
			}
		
			if(input != 1 && dividerCount > 0) {
				printf(" x ");
			}
		
			dividerCount = 0;
			}
		}
		
	printf("\n");
}

