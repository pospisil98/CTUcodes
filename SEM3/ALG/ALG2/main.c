#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#define TRUE 1
#define FALSE 0
 
int maxSum;
 
typedef struct {
    int y;
    int x;
} point;
 
typedef struct {
    int number;
    point from;
    point to;
    point ended;
    int isHorizontal;
    int sum;
} robot;
 
static void scanint(int *x) {
    register char c = getchar_unlocked();
    *x = 0;
    for (; (c < 48) || (c > 57);c = getchar_unlocked());
    for (; (c > 47) && (c < 58);c = getchar_unlocked())
        *x = (int)((((*x) << 1) + ((*x) << 3)) + c - 48);
}
 
int** allocateMatrix(int rows, int cols)
{
    int** matrix = (int **)malloc(rows * sizeof(int*));
    for (int i = 0; i < rows; i++)
    {
        matrix[i] = (int *)malloc(cols * sizeof(int));
    }
     
    return matrix;
}
 
point** allocateIntersectionMatrix(int rows, int cols)
{
    point** matrix = (point **)malloc(rows * sizeof(point*));
    for (int i = 0; i < rows; i++)
    {
        matrix[i] = (point *)malloc(cols * sizeof(point));
    }
     
    return matrix;
}
 
void freeMatrix(int** matrix, int rows, int cols)
{
    for(int i = 0; i < rows; i++) {
        free(matrix[i]);
    }
    free(matrix);
}
 
void fillMatrix(int** matrix, int rows, int cols, int user)
{
    if (user == TRUE)
    {
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                scanint(&matrix[i][j]);
            }
        }
    }
    else
    {
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                matrix[i][j] = FALSE;
            }
        }
    }
     
}
 
void printMatrix(int** matrix, int rows, int cols)
{
    for (int i = 0; i < rows; i++)
    {
        for (int j = 0; j < cols; j++)
        {
            printf("%d ", matrix[i][j]);
        }
        printf("\n");
    }
    printf("\n\n");
}
 
robot* loadRobots(int count)
{
    robot* robots = malloc(count * sizeof(robot));
    int a, b, c, d, isHorizontal;
     
    for (int  i = 0; i < count; i++)
    {
        scanint(&a);
        scanint(&b);
        scanint(&c);
        scanint(&d);
         
        point start = {a, b};
        point end = {c, d};
         
        if (a == c)
            isHorizontal = TRUE;
        else
            isHorizontal = FALSE;
             
        point p = {-1, -1};
         
        robot r = {i, start, end, p, isHorizontal, 0};
         
        robots[i] = r;
    }
     
    return robots;
}
 
void printRobo(robot r)
{
    printf("ROBO START\n");
    printf("Number %d From (%d, %d)  To (%d, %d)  Ended(%d, %d)\n", r.number, r.from.x, r.from.y, r.to.x, r.to.y, r.ended.x, r.ended.y);
    printf("isHorizontal= %d    sum= %d", r.isHorizontal, r.sum);
    printf("ROBO END\n");
}
 
void printRobots(int count, robot* robots)
{
    for (int i = 0; i < count; i++)
    {
        printf("Robot %d: ", i);
         
        printf("From [%d,%d] to [%d, %d] and isHorizontal=%d\n", robots[i].from.y, robots[i].from.x, robots[i].to.y, robots[i].to.x, robots[i].isHorizontal);
    }
}
 
 
void swap(robot* robots, int a, int b) 
{ 
    robot temp;
      
    temp = robots[a]; 
    robots[a] = robots[b]; 
    robots[b] = temp; 
} 
 
void walkRobot(int** passes, robot* robot, int** matrix)
{
    int sum = 0;
    if (robot->isHorizontal == TRUE)
    {
        int i = robot->from.x;
         
        if (robot->from.x < robot->to.x)
        {
            while (i <= robot->to.x)
            {
                if (passes[robot->from.y][i] == FALSE)
                {
                    passes[robot->from.y][i] = TRUE;
                     
                    sum += matrix[robot->from.y][i];
                }
                else
                {
                    break;
                }   
                i++;
            }
            i--;
        }
        else
        {
            while (i >= robot->to.x)
            {
                if (passes[robot->from.y][i] == FALSE)
                {
                    passes[robot->from.y][i] = TRUE;
 
                    sum += matrix[robot->from.y][i];
                }
                else
                {
                    break;
                }   
                i--;
            }
            i++;
        }
         
        point p = {robot->from.y, i};
        robot->ended = p;
    }
    else
    {
        int i = robot->from.y;
         
        if (robot->from.y < robot->to.y)
        {
            while (i <= robot->to.y)
            {
                if (passes[i][robot->from.x] == FALSE)
                {
                    passes[i][robot->from.x] = TRUE;
                     
                    sum += matrix[i][robot->from.x];
                }
                else
                {
                    break;
                }   
                i++;
            }
            i--;
        }
        else
        {
            while (i >= robot->to.y)
            {
                if (passes[i][robot->from.x] == FALSE)
                {
                    passes[i][robot->from.x] = TRUE;
                     
                    sum += matrix[i][robot->from.x];
                }
                else
                {
                    break;
                }   
                i--;
            }
            i++;
        }
         
        point p = {i, robot->from.x};
        robot->ended = p;
         
    }
     
    robot->sum = sum;
}
 
void walkRobotBack(int** passes, robot robot)
{
    if(robot.ended.x == -1 && robot.ended.y == -1)
        return;
     
    if (robot.isHorizontal == TRUE)
    {
        int i = robot.ended.x;
         
        if (robot.from.x < robot.ended.x)
        {
            while (i >= robot.from.x)
            {
                passes[robot.from.y][i] = FALSE;
                i--;
            }
        }
        else
        {
            while (i <= robot.from.x)
            {
                passes[robot.from.y][i] = FALSE;
                i++;
            }
        }       
    }
    else
    {
        int i = robot.ended.y;
         
        if (robot.from.y < robot.to.y)
        {
            while (i >= robot.from.y)
            {
                passes[i][robot.from.x] = FALSE;
                i--;
            }
        }
        else
        {
            while (i <= robot.from.y)
            {
                passes[i][robot.from.x] = FALSE;
                i++;
            }
        }
    }
     
}
 
void permute(robot* robots, int start, int end, int** passes, int** matrix) 
{ 
   int i; 
   if (start == end) 
   {
    //robots[end].sum = walkRobot(passes, &robots[end], matrix);
     
    int tempSum = 0;
     
        for (int i = 0; i < (end + 1); i++)
    {
         
        walkRobot(passes, &robots[i], matrix);
        //printRobo(robots[i]);
        //printMatrix(passes, 6, 6);
        //printf("\n");
             
        tempSum += robots[i].sum;
    }
     
     
    for (int i = end; i >= 0; i--)
    {
        walkRobotBack(passes, robots[i]);
    }
     
    //printf("tempSum = %d\n", tempSum);
     
    if (tempSum > maxSum)
        maxSum = tempSum;
   }
   else
   { 
       for (i = start; i <= end; i++) 
       { 
          swap(robots, start, i);
          //robots[i].sum = walkRobot(passes, &robots[i], matrix);
          permute(robots, start+1, end, passes, matrix);
          //walkRobotBack(passes, robots[i]);
          swap(robots, start, i); 
       } 
   }
}
 
int main(int argc, char **argv)
{
    int rows, cols, numOfRobots;
    maxSum = 0;
    int** matrix;
    int** passes;
    robot* robots;
 
    scanint(&rows);
    scanint(&cols);
     
    matrix = allocateMatrix(rows, cols);
    passes = allocateMatrix(rows, cols);
     
    fillMatrix(matrix, rows, cols, TRUE);
    fillMatrix(passes, rows, cols, FALSE);
     
     
    scanint(&numOfRobots);
    robots = loadRobots(numOfRobots);   
    //printRobots(numOfRobots, robots);
 
    permute(robots, 0, numOfRobots-1, passes, matrix); 
     
    //printf("\nMax sum is %d\n", maxSum);
     
    printf("%d", maxSum);
     
    freeMatrix(matrix, rows, cols);
    freeMatrix(passes, rows, cols);
    free(robots);
}