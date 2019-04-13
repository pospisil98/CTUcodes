#include <stdio.h>
#include <stdlib.h>
 
int stringLen(char* string)
{
    int len = 0;
    int index = 0;
 
    while(string[index] != '\0') {
        index++;
        len++;
    }
 
    return len;
}
 
char findRegexChar(char* pattern, int* position)
{
    int len = stringLen(pattern);
     
    for(int i = 0; i < len; i++) {
        if(pattern[i] == '*') {
            *position = i;
            return '*';
        } else if(pattern[i] == '+') {
            *position = i;
            return '+';
        } else if(pattern[i] == '?') {
            *position = i;
            return '?';
        }
    }
    return '\0';
}
 
int lookForPatternRegex(FILE* file, char* pattern, char*line, int* linePos, char regChar, int regPos)
{
    /* 
        return -1 on \n
        return -2 on EOF
        return 0 on not found
        return 1 on found
        return 2 on found of first letter - we need to start again - from main WE DONT WANT RECURSION
     */
     
    //printf("PAT: %s\n", pattern);
    //printf("REGCHAR: %c\n", regChar);
     
    // want we to count regexSign and preceding char? Probably not
    int patternLen = stringLen(pattern) - 2;
    char precedingChar = pattern[regPos - 1];
    char followingChar = pattern[regPos + 1];
    char c;
    int posBef;
    //int patternPosBef;
    char patternPos = 1;
    (*linePos)++;
      
    // get to the position where the char before regex char is (--char-- precedingChar regexChar)
    for(int i = 1; i < regPos - 1; i++) {
        c = fgetc(file);
        //printf("CHAR DO POZICE: %c\n", c);
 
        //printf("Posun char: %c\n", c);
 
        if(c == '\n') {
            return -1;
        } else if(c == EOF) {
            return -2;
        } else{
            line[*linePos] = c;
            (*linePos)++;
            patternPos++;
        }
 
        // i've increased to linePos val before
        int posBef = *linePos;
        posBef--;
 
        // it doesn't match pattern pos but it matches the first letter
        if(pattern[0] == line[posBef] && pattern[i] != line[posBef]) {
            return 2;
        }
         
        // if it doesn't match then it's not the pattern
        if(pattern[i] != line[posBef]) {
            return 0;
        }
         
     }
      
     //printf("Line befRegPos %s\n", line);
      
    /*
        + - 1 or more preceding
        * - 0 or more preceding
        ? - 0 or 1 times preceding
    */
     
    // -------- DEAL  WITH REGEX -------------------------------------------
    // we are on position (... char --precedingChar-- regexChar char...
    c = fgetc(file);
    //printf("Char kterej rovnou ukladam: '%c'\n", c);
    line[*linePos] = c;
    (*linePos)++;
 
    posBef = *linePos;
    posBef--;
 
    if(regChar == '+') {
        // 1 or more preceding
         
        // checkage if atleast one preceding
        if(line[posBef] != precedingChar) {
            return 0;
        }
         
        // skip all chars same as preceding one
        while((c = fgetc(file)) == precedingChar) {
            line[*linePos] = c;
            (*linePos)++;
        }   
         
        // condition for char != precedingChar
        if(c != followingChar) {
            return 0;
        } else {
            line[*linePos] = c;
            (*linePos)++;
        }
    } else if(regChar == '*') {
        // 0 or more preceding
        if(c != followingChar) {
            // skip all chars same as preceding one or don't do anything
            while((c = fgetc(file)) == precedingChar) {
                //printf("C: %c\n", c);
                line[*linePos] = c;
                (*linePos)++;
            }
         
            // condition for char != precedingChar
            if(c != followingChar) {
                return 0;
            } else {
                line[*linePos] = c;
                (*linePos)++;
            }   
        }
    } else if(regChar == '?') {
        // 0 or 1 preceding
         
        // case 0
        if(line[posBef] != precedingChar) {
            if(line[posBef] != followingChar) {
                return 0;
            }
        } else {
            c = fgetc(file);
             
            if(c != followingChar) {
                return 0;
            }
             
            line[*linePos] = c;
            (*linePos)++;
        }       
    }
     
    // for to the end of Pattern
     
    for(int i = patternPos + 1; i < patternLen; i++) {
        c = fgetc(file);
 
        if(c == '\n') {
            return -1;
        } else if(c == EOF) {
            return -2;
        } else{
            line[*linePos] = c;
            (*linePos)++;
            patternPos++;
        }
 
        // i've increased to linePos val before
        int posBef = *linePos;
        posBef--;
 
        // it doesn't match pattern pos but it matches the first letter
        if(pattern[0] == line[posBef] && pattern[i] != line[posBef]) {
            return 2;
        }
         
        // if it doesn't match then it's not the pattern
        if(pattern[i] != line[posBef]) {
            return 0;
        }
    }
    return 1;
}
 
int lookForPattern(FILE* file, char* pattern, char* line, int* linePos, int* tempFoundPos)
{
    /* looks for pattern in file because we found first letter
       return -1 on \n
       return -2 on EOF
       return 0 on not found
       return 1 on found
       return 2 on found of first letter - we need to start again - from main WE DONT WANT RECURSION
     */
      
    int patternLen = stringLen(pattern);
    char c;
    (*linePos)++;
     
    for(int i = 1; i < patternLen; i++) {
        c = fgetc(file);
         
        if(c == '\n') {
            return -1;
        } else if(c == EOF) {
            return -2;
        } else {
            line[*linePos] = c;
            (*linePos)++;
        }
         
        int posBef = *linePos;
        posBef--;
         
        if(pattern[i] != line[posBef]) {
            if(pattern[0] == line[posBef]) {
                *tempFoundPos = posBef;
                //(*linePos)++;
                return 2;
            }
            //(*linePos)++;
            return 0;
        }
        //printf("Nasel jsem\n");
    }
     
    return 1;
}
 
char* readLine(FILE* file, int* size, char* pattern, int* startOfPattern, int* foundCount, int* foundCountTotally, int* eof, int regexSwitch)
{
    /* Reads line to the dynamically sized array which it returns */
    char* line = (char*)malloc(*size * sizeof(char));
    char c;
    int linePos = 0;
    int lineEndPattern = 0;
     
    int regPos;
    char regChar;
     
    if(regexSwitch == 1) {
        regPos = 0;
        regChar = findRegexChar(pattern, ®Pos);
         
        //printf("REGPOS: %d     REGCHAR: %c    PRECCHAR: %c\n", regPos, regChar, precedingChar);  
    }
     
     
    while((c = fgetc(file)) != '\n') {
        //printf("CHAR V READLINE: '%c'\n", c);
        if(c == EOF) {
            *eof = 1;
            break;
        }
         
        if(linePos < (*size - 1)) {
            line[linePos] = c;
        } else { // here it means that we filled initialy maked array for chars
            *size = (*size) * 2;
            line = (char*)realloc(line, *size);
            line[linePos] = c;
        }
         
        if(regexSwitch == 1) {
            if(line[linePos] == pattern[0]) {
                int found = 2;
             
                while(found == 2) {
                    found = lookForPatternRegex(file, pattern, line, &linePos, regChar, regPos);
                     
                    if(found == -2) {           // found EOF
                            *eof = 1;
                            break;
                        } else if(found == -1) {    // found \n
                            lineEndPattern = 1;
                            break;
                        } else if(found == 1) {     // found pattern
                             
                         
                            (*foundCount)++;
                            (*foundCountTotally)++;
                        } // else found first letter - repeats while or found nothing - continue
                        linePos--;
                }
                if(lineEndPattern == 1) {
                    lineEndPattern = 0;
                    break;
                }
            }           
        } else {
            // trivial function, without regex
            if(line[linePos] == pattern[0]) {
                // 2 means found first letter
                int found = 2;
                int tempFoundPos = linePos;
             
                while(found == 2) {
                    found = lookForPattern(file, pattern, line, &linePos, &tempFoundPos);
                     
                    if(found == -2) {           // found EOF
                        *eof = 1;
                        break;
                    } else if(found == -1) {    // found \n
                        lineEndPattern = 1;
                        break;
                    } else if(found == 1) {     // found pattern
                        startOfPattern[*foundCount] = tempFoundPos;
                        (*foundCount)++;
                        (*foundCountTotally)++;
                    } // else found first letter - repeats while or found nothing - continue
                    linePos--;
                }
                if(lineEndPattern == 1) {
                    lineEndPattern = 0;
                    break;
                }
            }
        }
        linePos++;
    }
     
    line[linePos] = '\0';
     
    return line;
}
 
void printColourLine(char* line, char* pattern, int* patternStarts)
{
    int basePatternLen = stringLen(pattern);
    char c;
    int index = 0;
    int patternIndex = 0;
    int currentPatternLen = 0;
     
    //TEMPORARY
    currentPatternLen = basePatternLen;
 
    while((c = line[index]) != '\0') {          
        if(patternStarts[patternIndex] != -1 && index == patternStarts[patternIndex]) {
            printf("\033[01;31m\033[K");
        }
         
        printf("%c", c);
         
        if(patternStarts[patternIndex] != -1 && index == (patternStarts[patternIndex] + currentPatternLen - 1)) {
            patternIndex++;
            printf("\033[m\033[K");
        }
        index++;
    }
    printf("\n");
}
 
int strMatch(char* str1, char* str2)
{
    int strLen1 = stringLen(str1);
    int strLen2 = stringLen(str2);
      
    if(strLen1 != strLen2) {
        return 0;
    }
     
    for(int i = 0; i < strLen1; i++) {
        if(str1[i] != str2[i]) {
            return 0;
        }
    }
     
    return 1;
}
 
void processArgs(int argc,char** argv, int* stdinSwitch, int *regexSwitch, int* colorSwitch)
{
    char* regex = "-E";
    char* color = "--color=always";
 
    for(int i = 1; i < argc; i++) {
        //printf("ARGV: %s", argv[i]);
        if(strMatch(argv[i], regex) == 1) {
            *regexSwitch = 1;
        }  else if(strMatch(argv[i], color) == 1)
        {
            *colorSwitch = 1;
        } 
    }
}
 
char* readLineForRegex(FILE* file, int* eof)
{
    int size = 128;
     
    char* line = (char*)malloc(size * sizeof(char));
    char c;
    int linePos = 0;
     
    while((c = fgetc(file)) != '\n') {
        if(c == EOF) {
            *eof = 1;
            break;
        }
         
        if(linePos < (size - 1)) {
            line[linePos] = c;
        } else {
            size = size * 2;
            line = (char*)realloc(line, size);
            line[linePos] = c;
        }
         
        linePos++;
    }
    return line;
}
 
/* The main program */
int main(int argc, char *argv[])
{
    FILE* file = NULL;          // file pointer
    int maxSize = 128;          // initial value of max line size
    char* line;                 // line char array
    int lineCounter = 0;        // line counter for whatever
    int startOfPattern[100] = {-1};     // array for numbers where patter starts on line because of the colours
    int foundCount = 0;         // number of found patterns
    int foundCountTotally = 0;  // number of found patterns totally
    int eofCheck = 0;           // for usage in readLine to determine if its end of file
     
     
    int stdinSwitch = 0;
    int regexSwitch = 0;
    int colorSwitch = 0;        
    processArgs(argc, argv, &stdinSwitch, ®exSwitch, &colorSwitch);
     
    if(argc < 2) {
        printf("Not enough arguments");
        return 100;
    }   
 
    char* pattern = argv[argc - 2];
    char* filePath = argv[argc - 1];
 
    if((file = fopen(filePath, "r")) == NULL) {
        fprintf(stderr, "Error: cannot open file â€™%sâ€™\n", filePath);
        return 100;
    }
     
    //printf("File opened\n");
     
    while(eofCheck != 1) {
        lineCounter++;
        line = readLine(file, &maxSize, pattern, startOfPattern, &foundCount, &foundCountTotally, &eofCheck, regexSwitch);
         
        if(foundCount > 0) {
            foundCountTotally++;            
             
            if(colorSwitch == 1) {
                printColourLine(line, pattern, startOfPattern);
            } else {
                printf("%s\n", line);
            }
             
            //printColourLine(line, pattern, startOfPattern);
            //printf("%s\n", line);
             
            foundCount = 0;
        }
        free(line);
    }
     
    if(fclose(file) == EOF) {
        fprintf(stderr, "Error: cannot close file â€™%sâ€™\n", filePath);
        return 100;
        //printf("File closed\n");
    }
     
    // when we dont find anything
    if(foundCountTotally == 0) {
        return 1;
    }
     
  return 0;
}