#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int compare(const char *str1, const char *str2)
{
    int index = 0;
    int sameCount = 0;
    char c1, c2;
    while ((c1 = str1[index]) != '\0') {
        c2 = str2[index];
        if (c1 == c2) {
            sameCount++;
        }
        index++;
    }
    return sameCount;
}

char rotate(char original, int offset)
{
    int new = (int)original;
    int counter = 0;

    while (counter < offset) {
        new += 1;

        if (new == ('Z' + 1)) {
            new = 'a';
        } else if (new == ('z' + 1)) {
            new = 'A';
        }
        counter++;
    }
    return new;
}

void shift(const char *src, char *dst, int offset)
{
    int index = 0;
    char c;
    while ((c = src[index]) != '\0') {
        dst[index] = rotate(c, offset);
        index++;
    }
    dst[index] = '\0';
}

char *readString(char *destination, int size, int *errDestination)
{
    int current_size = size;
    int index = 0;
    int c;

    while ((c = getchar()) != '\n' && c != EOF) {
        if (index == current_size) {
            current_size += 50 * sizeof(char);

            char *temp = realloc(destination, current_size);
            if (temp == NULL) {
                break;
            } else {
                destination = temp;
            }
        }

        destination[index++] = (char)c;

        if ((c > 'Z' && c < 'a') || (c < 'A' || c > 'z')) {
            *errDestination += 1;
        }
    }
    destination[index] = '\0';

    return destination;
}

int returnStringLen(char *string)
{
    int index = 0;
    char c;
    while ((c = string[index]) != '\0') {
        index++;
    }

    return index;
}

int findMin(int a, int b, int c)
{
    if (a <= b && a <= c) {
        return a;
    } else if (b <= a && b <= c) {
        return b;
    } else {
        return c;
    }
}

int levenshteinDistance(char *string1, char *string2)
{	
	int str1len = returnStringLen(string1);
    int str2len = returnStringLen(string2);
    int subCost = 0, del = 0, ins = 0, sub = 0;
    int oldDiag = 0;
	int column[str1len+1];
 	
 	for (int i = 1; i <= str1len; i++) {
        column[i] = i;
       }
       
    for (int i = 1; i <= str2len; i++) {
        column[0] = i;
        int lastDiag = i - 1;
        for (int j = 1; j <= str1len; j++) {
            oldDiag = column[j];
            
            if (string1[j - 1] == string2[i - 1]) {
                subCost = 0;
            } else {
                subCost = 1;
            }

            del = column[j] + 1;
            ins = column[j-1] + 1;
            sub = lastDiag + subCost;

            column[j] = findMin(del, ins, sub);
            
            lastDiag = oldDiag;
        }
    }
    return(column[str1len]);
}

int main(int argc, char *argv[])
{
    int size = 50 * sizeof(char) + 1;

    int encryptedErrValue = 0;
    int *encryptedErr = &encryptedErrValue;

    char *encrypted = malloc(size);
    encrypted = readString(encrypted, size, encryptedErr);

    int heardErrValue = 0;
    int *heardErr = &heardErrValue;

    char *heard = malloc(size + 1);
    heard = readString(heard, size, heardErr);

    int encryptedSize = returnStringLen(encrypted);
    int heardSize = returnStringLen(heard);
	
	int bigger = 0;
	(encryptedSize > heardSize) ? (bigger = encryptedSize) : (bigger = heardSize);
	
    char *dest = malloc(bigger + 1);
    dest[0] = '\0';

    char *decrypted = malloc(bigger + 1);
    decrypted[0] = '\0';

    if (*encryptedErr > 0 || *heardErr > 0) {
        fprintf(stderr, "Error: Chybny vstup!\n");

        free(encrypted);
        free(heard);
        free(dest);
        free(decrypted);

        return 100;
    }

    if (argc >= 2 && strcmp(argv[1], "-prp-optional") == 0) {
        int minLevenshtein = heardSize;
    	int tempLevenshtein = heardSize;
    	for (int offset = 1; offset <= 52; offset++) {
            shift(encrypted, dest, offset);
            tempLevenshtein = levenshteinDistance(heard, dest);
            
            //printf("\nLEV %i OFFSET %i\n", tempLevenshtein, offset);

            if (tempLevenshtein < minLevenshtein) {
                minLevenshtein = tempLevenshtein;

                for (int i = 0; i <= encryptedSize; i++) {
                    if (i != encryptedSize) {
                        decrypted[i] = dest[i];
                    } else {
                        decrypted[i] = '\0';
                    }
                }
            }
        }
        //printf("%i", minLevenshtein);
    } else {
    	if (heardSize != encryptedSize) {
            fprintf(stderr, "Error: Chybna delka vstupu!\n");

            free(encrypted);
            free(heard);
            free(dest);
            free(decrypted);

            return 101;
        }
		
		int maxSame = 0;
        int tempSameCount = 0;
        for (int offset = 1; offset <= 52; offset++) {
            shift(encrypted, dest, offset);
            tempSameCount = compare(heard, dest);

            if (tempSameCount > maxSame) {
                maxSame = tempSameCount;
                tempSameCount = 0;

                for (int i = 0; i <= encryptedSize; i++) {
                    if (i != encryptedSize) {
                        decrypted[i] = dest[i];
                    } else {
                        decrypted[i] = '\0';
                    }
                }
            }
        }
    }

    printf("%s\n", decrypted);

    free(encrypted);
    free(heard);
    free(dest);
    free(decrypted);
    return 0;
}
