#include <stdio.h>
#include <stdlib.h>

#define ERR -1
#define ADD 1
#define SUB 2
#define MUL 3

typedef struct matrix {
	int rows;
	int cols;
	int** data;
} matrix;

void printMatrix(matrix m) {
	printf("%d %d\n", m.rows, m.cols);
	for(int  r = 0; r < m.rows; r++) {
		for(int c = 0; c < m.cols; c++) {
			if(c < (m.cols -1))
				printf("%i ", m.data[r][c]);
			else
				printf("%i\n", m.data[r][c]);
		}
	}
}

matrix readMatrix(int rows, int cols) {
	int scanned;
	int scan;
	
	matrix m;
	m.rows = rows;
	m.cols = cols;
	
	//printf("\nBudu cist rows %d cols %d\n", rows, cols);
	
	// create array for rows and cols
	int** data = (int**)malloc(rows * sizeof(int*));
	
	// full data with row arrays with len of cols
	for(int i = 0; i < rows; i++) {
		data[i] = (int*)malloc(cols * sizeof(int));
	}
	
	m.data = data;
	
	for(int r = 0; r < rows; r++) {
		for(int c = 0; c < cols; c++) {
			scanned = scanf("%d", &scan);
			
			if(scanned == 1) {
				data[r][c] = scan;
			} else {
				//printf("Je tu problem!!\n");
				m.cols = -1;
				return m;
			}
		}
	}
	
	m.data = data;
	
	//printf("\nNactena matice ma rows %d cols %d\n", m.rows, m.cols);
	//printMatrix(m);
	return m;
}

matrix readCompleteMatrix() {
	/* reads matrix completly, if fault returns matrix with cols -1 */
	int scanned;
	int rows;
	int cols;
	matrix m;
	
	scanned = scanf("%d %d", &rows, &cols);
	//printf("\nNacteno rows %d cols %d\n", rows, cols);
	
	if(scanned != 2 || rows <= 0 || cols <= 0){
		m.cols = -1;
		return m;
	}
	
	m = readMatrix(rows, cols);
	
	return m;
}

void freeMatrix(matrix m) {
	/* frees complete matrix from memory */
	for(int i = 0; i < m.rows; i++) {
		free(m.data[i]);
	}
	free(m.data);
}

int readOperation() {
	/* Returns number of operation */
	int scanned;
	char input;
	int ret = ERR;
	
	scanned = scanf("\n%c", &input);
	
	if(scanned == EOF) {
		ret = -100;
	} else {
		if((input != '+' && input != '-' && input != '*') || scanned != 1) {
			ret = ERR;
		} else {
			if(input == '+')
				ret = ADD;
			else if(input == '-')
				ret = SUB;
			else
				ret = MUL;
		}
	}
	//printf("ZNAMENKO JE %d\n", ret);
	return ret;
}

matrix addMatrix(matrix m1, matrix m2) {
	/* adds two valid matrices */
	matrix b;
	b.rows = m1.rows;
	b.cols = m1.cols;
	b.data = (int**)malloc(b.rows * sizeof(int*));
	for(int i = 0; i < b.rows; i++) {
		b.data[i] = (int*)malloc(b.cols * sizeof(int));
	}
		
	for(int r = 0; r < b.rows; r++) {
		for(int c = 0; c < b.cols; c++) {
			b.data[r][c] = m1.data[r][c] + m2.data[r][c];
		}	
	}
	
	return b;
}

matrix subMatrix(matrix m1, matrix m2) {
	/* substracts two valid matrices */
	matrix b;
	b.rows = m1.rows;
	b.cols = m1.cols;
	b.data = (int**)malloc(b.rows * sizeof(int*));
	for(int i = 0; i < b.rows; i++) {
		b.data[i] = (int*)malloc(b.cols * sizeof(int));
	}
	
	for(int r = 0; r < b.rows; r++) {
		for(int c = 0; c < b.cols; c++) {
			b.data[r][c] = m1.data[r][c] - m2.data[r][c];
		}	
	}
	
	return b;
}

matrix mulMatrix(matrix m1, matrix m2) {
	/* multiplies two valid matrices */
	matrix b;
	
	b.rows = m1.rows;
	b.cols = m2.cols;
	//printf("ALOKUJUUUU\n");
	b.data = (int**)malloc(b.rows * sizeof(int*));
	for(int i = 0; i < b.rows; i++) {
		b.data[i] = (int*)malloc(b.cols * sizeof(int));
	}
	
	for (int i = 0; i < m1.rows; i++) {
        for (int j = 0; j < m2.cols; j++) {
            b.data[i][j] = 0;
            for (int k = 0; k < m1.cols; k++)
                b.data[i][j] += m1.data[i][k] * m2.data[k][j];
        }
    }
    
    return b;
}

int checkAddSub(int r1, int c1, int r2, int c2) {
	/* 1 if possible -1 if not */
	int ret = 0;
	if(r1 != r2 || c1 != c2) {
		ret = ERR;
	} else {
		ret = 1;
	}
		
	return ret;
}

int checkMul(int r1, int c1, int r2, int c2) {
	/* 1 if possible -1 if not */
	int ret = 0;
	if(c1 == r2) {
		ret = 1;
	} else {
		ret = ERR;
	}
	
	return ret;
}

matrix performOperation(matrix m1, matrix m2, int operation) {
	/* universal function to perform mathematical operation */
	matrix r;
	if(operation == ADD) {
		r = addMatrix(m1, m2);
	} else if(operation == SUB) {
		r = subMatrix(m1, m2);
	} else if(operation == MUL) {
		r = mulMatrix(m1, m2);
	}
	return r;
}

int checkOperation(matrix m1, matrix m2, int operation) {
	/* returns -1 if not possible, 1 if possible */
	int check;
	if(operation == ADD || operation == SUB) {
		check = checkAddSub(m1.rows, m1.cols, m2.rows, m2.cols);
	} else {
		check = checkMul(m1.rows, m1.cols, m2.rows, m2.cols);
	}
	return check;
}

matrix processMul(matrix m, int* operation) {
	/* should return one matrix - result of all mults continuously behind each other */	
	//printf("\nProcessMUL START\n");
	matrix next;
	matrix res;
	int op;
	int counter = 0;
	
	res = m;
	
	do 
	{
		next = readCompleteMatrix();
		
		if(next.cols != -1) {
			/*printf("\nMatice RES\n");
			printMatrix(res);
			printf("\nMatice NEXT\n");
			printMatrix(next);
			printf("\nCheckOperation\n");
			printf("%i", checkOperation(res, next, MUL));*/
			if(checkOperation(res, next, MUL) == 1) {
				// abych mohl m1 uvolnit o level vejš
				if(counter == 0) {
					res = mulMatrix(res, next);
				} else {
					matrix toFree;
					toFree = res;
					res = mulMatrix(toFree, next);
					//printf("\nUvolnuji TO FREE RES\n");
					freeMatrix(toFree);		
				}
			} else {
				//printf("Vyjeb 1");
				freeMatrix(next);
				res.cols = -1;
				break;
			}	
		} else {
			//printf("Vyjeb 2");
			freeMatrix(next);
			res.cols = -1;
			break;	
		}
		counter++;
		//printf("\nUvolnuji next\n");
		freeMatrix(next);
	} while ((op = readOperation()) == MUL);
	
	*operation = op;
	
	//freeMatrix(next);
	
	//printf("\nProcessMUL END\n");
	return res;
}

/* The main program */
int main(int argc, char *argv[])
{ 	
	matrix m1, res, mulRes;
	int o1, nextO, check;
	int counter = 0;
	int mulCount = 0;

	res = readCompleteMatrix();
	if(res.cols == -1) {
		freeMatrix(res);
		fprintf(stderr, "Error: Chybny vstup!\n");
		return 100;
	}
	
	o1 = readOperation();
	
	if(o1 == -1) {
		freeMatrix(res);
		fprintf(stderr, "Error: Chybny vstup!\n");
		return 100;
	}
	
	// loop for scanning - just remove while
	while(1){
		/*printf("\nPrubezny vysledek\n");
		printMatrix(res);
		printf("\n");*/
	
		if(counter != 0) {
			matrix toFree;
			toFree = m1;
			freeMatrix(toFree);
		}
		
		m1 = readCompleteMatrix();
	
		if(m1.cols == -1) {
			freeMatrix(res);
			freeMatrix(m1);
			fprintf(stderr, "Error: Chybny vstup!\n");
			return 100;
		}
	
		nextO = readOperation();
		//printf("Operace je: %d\n", nextO);
		if(nextO == -1) {
			freeMatrix(res);
			freeMatrix(m1);
			fprintf(stderr, "Error: Chybny vstup!\n");
			return 100;
		}
		
		//printf("\nM1 pred if mul\n");
		//printMatrix(m1);
	
		if(nextO == MUL) {
			// matrix mulRes;
			/*if(mulCount != 0) {
			matrix toFree;
			toFree = mulRes;		
			freeMatrix(toFree);
			}*/
		
			mulRes = processMul(m1, &nextO);
			
			if(mulRes.cols == -1) {
				freeMatrix(res);
				freeMatrix(mulRes);
				//freeMatrix(m1);
				fprintf(stderr, "Error: Chybny vstup!\n");
				return 100;
			}
		
			/*printf("\nMULRES\n");
			printMatrix(mulRes);
			printf("\n");*/
		
			check = checkOperation(res, mulRes, o1);
		
			if(check == -1) {
				freeMatrix(mulRes);
				freeMatrix(res);
				freeMatrix(m1);
				fprintf(stderr, "Error: Chybny vstup!\n");
				return 100;	
			}
		
			matrix toFree;
			toFree = res;
		
			res = performOperation(res, mulRes, o1);
		
		
			mulCount++;
			//printf("\nfree res - tofree\n");
			freeMatrix(toFree);
			//printf("\nfree mulRes\n");
			freeMatrix(mulRes);
			
			//printf("\nM1 vevnitr\n");
			//printMatrix(m1);
		} else {
			check = checkOperation(res, m1, o1);
		
			if(check == -1) {
				freeMatrix(res);
				freeMatrix(m1);
				fprintf(stderr, "Error: Chybny vstup!\n");
				return 100;	
			}
			
			matrix toFree;
			toFree = res; 
			res = performOperation(res, m1, o1);
		
			freeMatrix(toFree);
		}	
	
		// jump out of while when operation === EOF
		if(nextO == -100) {
			//freeMatrix(m1);
			break;
		}
		
		o1 = nextO;
		counter++;
	// end of while
	}
	
	printMatrix(res);
	
	freeMatrix(res);
	freeMatrix(m1);
	return 0;
}

