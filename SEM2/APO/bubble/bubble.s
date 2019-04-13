#define t0 $8
#define t1 $9
#define t2 $10
#define t3 $11
#define t4 $12

#define s0 $16
#define s1 $17
#define s2 $18
#define s3 $19

#define n $1
#define i $2
#define k $3
#define tmp $4
#define kN $5
#define nO $6
#define one $7
#define lt $8

.globl    pole
.data
.align    2

pole:
.word    5,3,4,1,2

.text
.globl start
.ent start

/*
int pole[5]={5,3,4,1,2};				// mám
int main()
{	
	int N = 5,i,j,tmp;     				// Hotovo
	for(i=0; i<N; i++)					//
		for(j=0; j<N-1-i; j++)			//
			if(pole[j+1]<pole[j])		//
			{
				tmp = pole[j+1];		//
				pole[j+1] = pole[j];	//
				pole[j] = tmp;			//
			}
	return 0;							//
}
*/


start:
	LA   s0, pole

	ADDI n, $0, 5				// int n = 5;
	ADD	 i, i, $0				// int i = 0;
	ADD  k, k, $0				// int k = 0;
	ADD  tmp, tmp, $0 			// int tmp = 0;
	
	ADDI one, $0, 1				// regiser with one
	SUB nO, n, $7				// nWithoutOne -= 1
	SUB  kN, nO, i				// kn = nO - i

	
	FORO:
	  BEQ  i, n, DONEO  		// If i==n, ukonceni FORO
	  SUB  kN, nO, i			// kn = nO - i
	  
	  FORI:
	  	BEQ k, kN, DONEI 		// If k == kN, ukonceni FORI
	  		
	  	LW   s1, 0x0(s0)		// load array[j]
	  	ADDI s0, s0, 0x4		// "j"++
	  	LW   s2, 0x0(s0)		// load j+1
	  	
	  	
	  	SLT s2, s1, lt
	  	BEQ lt, $0, OK
	  	  	
	  	SWAP:  		
	  		SW s1 , 0x(s0)
	  		SUB s0, s0, one
	  		SW s1, 0x(s0)
	  	
	  	OK:
	  		ADD tmp, $0, $0		// reset values
	  		ADD s1, $0, $0
	  		ADD s2, $0, $0
	  		ADDI s0, s0, 1
	  		ADDI k, k, 1		// k += 1
	  		
	  		J FORI
	  DONEI:
	  
	  LA   s0, pole		//reset s0?
	  
	  ADDI i, i, 1     // i += 1
	  J    FORO
	DONEO:
	

nop
.end start