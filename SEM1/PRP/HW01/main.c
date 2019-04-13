#include <stdio.h>
#include <stdlib.h>

/* The main program */
int main(int argc, char *argv[])
{
  int input1 = 0;
	int input2 = 0;
	int numbersScanned = 0;

	numbersScanned = scanf("%d %d", &input1, &input2);

	// should we treat bad input?? if no then remove numbersScanned from condition
	if(input1 > 10000 || input1 < -10000 || input2 > 10000 || input2 < -10000 || numbersScanned != 2)
	{
		printf("Vstup je mimo interval!\n");
	} else {

		printf("Desitkova soustava: %i %i\n", input1, input2);
		printf("Sestnactkova soustava: %x %x\n", input1, input2);
		printf("Soucet: %i + %i = %i\n", input1, input2, input1 + input2);
		printf("Rozdil: %i - %i = %i\n", input1, input2, input1 - input2);
		printf("Soucin: %i * %i = %i\n", input1, input2, input1 * input2);

		if(input2 == 0)
		{
			printf("Nedefinovany vysledek!\n");
		} else {
			printf("Podil: %i / %i = %i\n",input1, input2, input1 / input2);
		}
		
		printf("Prumer: %.1f\n", (float)(input1 + input2)/ 2);
	}	
	return 0;
}

