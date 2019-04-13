CFLAGS+= -pedantic-errors -Wall -Werror -std=c99 -g -O0

HW=hw06

program: main.c 
	${CC} ${CFLAGS} main.c -o program
	
zip:
	zip ${HW}-us.zip main.c
	
clean:
	rm -f *.o
	rm -f program
	rm -f ${HW}-us.zip

.PHONY: clear zip
