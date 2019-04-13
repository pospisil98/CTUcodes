CFLAGS+= -pedantic -Wall -std=c99 -O0 -g
HW=hw07

program: grep.c 
	${CC} ${CFLAGS} grep.c -o program

zip:
	zip ${HW}-us.zip grep.c

clean:
	rm -f *.o
	rm -f program
	rm -f ${HW}-us.zip

.PHONY: clear zip
