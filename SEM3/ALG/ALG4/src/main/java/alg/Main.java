package alg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    public static int rows;
    public static int cols;
    
    public static int startRow;
    public static int startCol;
    
    public static int endRow;
    public static int endCol;
    
    public static int[][] inputValues;
    public static int[][] areaSizes;
    public static ArrayList<Integer> areaTempRow;
    public static ArrayList<Integer> areaTempCol;
    
    public static int[][] distances;
      
    public static int reconfigurationsNeeded;
    public static int simpleSteps;
    public static boolean searching = true;
    
    public static void main(String[] args) throws IOException {
        load();   
        //System.out.println("load");
        
        areaSizes = new int[rows][cols];
        areaTempRow = new ArrayList<>();
        areaTempCol = new ArrayList<>();
        distances = new int[rows][cols];
        
        fillDistances();
        //System.out.println("dist");
        
        getAreas();
        //System.out.println("AREAS");
        
        dijkstra(startRow, startCol);
        //System.out.println("DIJK");
        
        reconfigurationsNeeded = distances[endRow][endCol];
        
        simpleSteps = searchPathToEnd();
        
        System.out.println(reconfigurationsNeeded + " " + simpleSteps);
    }

    public static void load() throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        rows = Integer.valueOf(st.nextToken());
        cols = Integer.valueOf(st.nextToken());
        
        inputValues = new int[rows][cols];
        
        st = new StringTokenizer(br.readLine());
        
        startRow = Integer.valueOf(st.nextToken()) - 1;
        startCol = Integer.valueOf(st.nextToken()) - 1;
        
        st = new StringTokenizer(br.readLine());
        
        endRow = Integer.valueOf(st.nextToken()) - 1;
        endCol = Integer.valueOf(st.nextToken()) - 1;
        
        for (int i = 0; i < rows; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < cols; j++) {
                inputValues[i][j] = Integer.valueOf(st.nextToken());
            } 
        }      
    }
    
    static void fillDistances()
    {
        for (int[] arr : distances) {
            Arrays.fill(arr, Integer.MAX_VALUE);
        }
    }
    
    static void printMatrix(int[][] matrix)
    {
        for (int i = 0; i < rows; i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }
    }
    
    public static void DFS(int row, int col, int sectorValue)
    {
        if (row >= 0 && col >= 0 && row < rows && col < cols)
        {
            if (areaSizes[row][col] == 0 && inputValues[row][col] == sectorValue)
            {
                areaSizes[row][col] = -1;
                
                areaTempRow.add(row);
                areaTempCol.add(col);              
                
                DFS(row - 1, col, sectorValue);
                DFS(row, col + 1, sectorValue);
                DFS(row + 1, col, sectorValue);
                DFS(row, col - 1, sectorValue);
            }
        }
    }
    
    public static void getAreas() {
        // 0 - unvisited, 1 - opened, 2 - closed
        
        int areaIndex = 0;
        int areaSize = 0;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (areaSizes[i][j] == 0)
                {
                    DFS(i, j, inputValues[i][j]);
                    
                    for (int k = 0; k < areaTempRow.size(); k++) {
                        areaSizes[areaTempRow.get(k)][areaTempCol.get(k)] = areaTempRow.size();
                    }
                    
                    areaTempRow = new ArrayList<>();
                    areaTempCol = new ArrayList<>();
                }
            }
        }
        
    }
 
    public static void dijkstra(int row, int col)
    {
        int neighborRow, neighborCol, distToNew;
        int[] rowAdd = {-1, 0, 1, 0};
        int[] colAdd = {0, 1, 0, -1}; 
        Node current;
                
        PriorityQueue<Node> queue = new PriorityQueue<>();
        distances[row][col] = 0;
        
        queue.add(new Node(row, col, 0));
        
        while (!queue.isEmpty())
        {
            current = queue.poll();
            for (int i = 0; i < 4; i++)
            {
                neighborRow = current.row + rowAdd[i];
                neighborCol = current.col + colAdd[i];
                
                if (neighborRow >= 0 && neighborCol >= 0 && neighborRow < rows && neighborCol < cols)
                {
                    distToNew = 0;
                    if (Math.abs(areaSizes[current.row][current.col] - areaSizes[neighborRow][neighborCol]) > 
                            Math.min(areaSizes[current.row][current.col], areaSizes[neighborRow][neighborCol]))
                    {
                        distToNew = 1;
                    }
                    
                    if(distances[neighborRow][neighborCol] > distances[current.row][current.col] + distToNew)
                    {
                        distances[neighborRow][neighborCol] = distances[current.row][current.col] + distToNew;
                        queue.add(new Node(neighborRow, neighborCol, distances[neighborRow][neighborCol]));
                    }
                }
            }
        }      
    }

    public static int rowColToSingle(int row, int col)
    {
        return (row * cols) + col;
    }
    
    public static int singleToRows(int single)
    {
        return single / cols;
    }
    
    public static int singleToCols(int single)
    {
        return single % cols;
    }
    
    public static int searchPathToEnd() {
        // 0 - unvisited, 1 - opened, 2 - closed
        int[][] states = new int[rows][cols];
        int[][] reconfigs = new int[rows][cols];
        Queue<Node> q = new LinkedList<>();
        
        int neighborRow, neighborCol, currentRow, currentCol, testReconfig;
        int[] rowAdd = {-1, 0, 1, 0};
        int[] colAdd = {0, 1, 0, -1}; 
        Node poped = new Node(-1, -1, -1);
        
        states[startRow][startCol] = 0;
       
        q.add(new Node(startRow, startCol, 1));
        
        while (!q.isEmpty() && searching)
        {
            poped = q.poll();
            currentRow = poped.row;
            currentCol = poped.col;
                       
            for (int i = 0; i < 4; i++)
            {
                neighborRow = currentRow + rowAdd[i];
                neighborCol = currentCol + colAdd[i];                      
                
                if (neighborRow >= 0 && neighborCol >= 0 && neighborRow < rows && neighborCol < cols)
                {  
                    if (states[neighborRow][neighborCol] == 0)
                    {
                        
                        reconfigs[neighborRow][neighborCol] = reconfigs[currentRow][currentCol];

                        if (Math.abs(areaSizes[currentRow][currentCol] - areaSizes[neighborRow][neighborCol]) > 
                                Math.min(areaSizes[currentRow][currentCol], areaSizes[neighborRow][neighborCol]))
                        {
                            reconfigs[neighborRow][neighborCol]++;
                        }
                        
                        if (reconfigs[neighborRow][neighborCol] == distances[neighborRow][neighborCol])
                        {
                            if (reconfigs[neighborRow][neighborCol] <= reconfigurationsNeeded)
                            {                            
                                if (neighborRow == endRow && neighborCol == endCol)
                                {
                                    searching = false;
                                }

                                states[neighborRow][neighborCol] = 1;
                                q.add(new Node(neighborRow, neighborCol, poped.configs + 1));
                            }
                        }
                    }
                    else if (states[neighborRow][neighborCol] == 1)
                    {
                        testReconfig = reconfigs[currentRow][currentCol];

                        if (Math.abs(areaSizes[currentRow][currentCol] - areaSizes[neighborRow][neighborCol]) > 
                                Math.min(areaSizes[currentRow][currentCol], areaSizes[neighborRow][neighborCol]))
                        {
                            testReconfig++;
                        }
                        if (reconfigs[neighborRow][neighborCol] > testReconfig)
                        {
                            reconfigs[neighborRow][neighborCol] = testReconfig;
                        }
                    }
                }
            }
            states[currentRow][currentCol] = 2;
        }
        
        return poped.configs;
    }
}
