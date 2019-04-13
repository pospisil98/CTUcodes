#include <stdio.h>
#include <stdlib.h>

/* The main program */
int main(int argc, char *argv[])
{
  	int input;
	int positiveCount = 0;
	int negativeCount = 0;
	int evenCount = 0;
	int oddCount = 0;
	int max;
	int min;
	int sum = 0;
	int count = 0;
	
	while(scanf("%d", &input) != EOF)
	{
		if(input > 10000 || input < -10000) {
			printf("\nError: Vstup je mimo interval!\n");
			return 100;
		} else {
			count++;
			// manage print format and stuff in first iteration
			if(count == 1){
				printf("%d", input);
				max = input;
				min = input;
			} else
				printf(", %d", input);
			
			// increment the right sign count
			if(input > 0)
				positiveCount++;
			
			if (input < 0)
				negativeCount++;
				
			// increment the right category of numbers	
			if(input % 2 == 0)
				evenCount++;
			else
				oddCount++;
				
			// store sum of all numbers for the average of them
			sum += input;
			
			// block for min max identification
			if(max < input)
				max = input;
			
			if(min > input)
				min = input;
		}
	}
	
	printf("\nPocet cisel: %d", count);
	printf("\nPocet kladnych: %d", positiveCount);
	printf("\nPocet zapornych: %d", negativeCount);
	printf("\nProcento kladnych: %.2f", ((float)positiveCount/count)*100);
	printf("\nProcento zapornych: %.2f", ((float)negativeCount/count)*100);
	printf("\nPocet sudych: %d", evenCount);
	printf("\nPocet lichych: %d", oddCount);
	printf("\nProcento sudych: %.2f", ((float)evenCount/count)*100);
	printf("\nProcento lichych: %.2f", ((float)oddCount/count)*100);
	printf("\nPrumer: %.2f", ((float)sum/count));
	printf("\nMaximum: %d", max);
	printf("\nMinimum: %d\n", min);
	
	return 0;
}

