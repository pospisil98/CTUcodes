package alg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
    
    public static int rows, cols;
    
    public static int[][] inputMatrix;
    public static int[][] sumRightMatrix;
    public static int[][] sumLeftMatrix;
    public static int[][] sumTopMatrix;
    public static int[][] sumBotMatrix;
    
    public static void main(String[] args) throws IOException {       
        loadAndInit();
        makeSumMatrices();
        tryBranches();
    }
    
    private static void loadAndInit() throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        rows = Integer.valueOf(st.nextToken());
        cols = Integer.valueOf(st.nextToken());
        
        inputMatrix = new int [rows][cols];
        sumLeftMatrix = new int [rows][cols - 1];
        sumRightMatrix = new int [rows][cols - 1];
        sumTopMatrix = new int [rows - 1][cols];
        sumBotMatrix = new int [rows - 1][cols];

        for (int r = 0; r < rows; r++) {
            st = new StringTokenizer(br.readLine());
            
            for (int c = 0; c < cols; c++) {               
                inputMatrix[r][c] = Integer.valueOf(st.nextToken());      
            }
        }
    }
    
    private static void makeSumMatrices()
    {
        int maxTempSum = 0;
        int maxSum = Integer.MIN_VALUE;
            
        //----LEFT------
        for (int r = 0; r < rows; r++) {
            for (int c = 1; c < cols; c++) {
                for (int i = c; i < cols; i++) {
                    maxTempSum += inputMatrix[r][i];                    

                    if(maxTempSum > maxSum)
                        maxSum = maxTempSum;         
                } 

                sumLeftMatrix[r][c - 1] = maxSum;
                maxTempSum = 0;
                maxSum = Integer.MIN_VALUE;
            }
        }
        //----RIGHT------
        for (int r = rows - 1; r >= 0; r--) {
            for (int c = cols - 2; c >= 0; c--) {
                for (int i = c; i >= 0; i--) {
                    maxTempSum += inputMatrix[r][i];                    

                    if(maxTempSum > maxSum)
                        maxSum = maxTempSum;         
                } 

                sumRightMatrix[r][c] = maxSum;
                maxTempSum = 0;
                maxSum = Integer.MIN_VALUE;
            }
        }
        //----TOP------
        for (int r = 1; r < rows; r++) {
            // walk through every column (last one is pointless)
            for (int c = 0; c < cols; c++) {
                // start from every element
                for (int i = r; i < rows; i++) {
                    maxTempSum += inputMatrix[i][c];                    

                    if(maxTempSum > maxSum)
                        maxSum = maxTempSum;         
                } 

                sumTopMatrix[r - 1][c] = maxSum;
                maxTempSum = 0;
                maxSum = Integer.MIN_VALUE;
            }
        }
        //----BOT------
        for (int r = rows - 2; r >= 0; r--) {
            for (int c = cols - 1; c >= 0; c--) {
                for (int i = r; i >= 0; i--) {
                    maxTempSum += inputMatrix[i][c];                    

                    if(maxTempSum > maxSum)
                        maxSum = maxTempSum;         
                } 

                sumBotMatrix[r][c] = maxSum;
                maxTempSum = 0;
                maxSum = Integer.MIN_VALUE;
            }
        }
    }
    
    private static void tryNewBranchVal(int possibleVal, int[] arr) {
        if(possibleVal > arr[0])
            arr[0] = possibleVal;
        else
            return;
        
        Arrays.sort(arr);
    }

    private static void tryBranches() {
        int [] branchesA = {Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};
        int [] branchesB = {Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};
        
        int trunkSum;
        
        int tempSum = 0;
        int maxSum = Integer.MIN_VALUE;
        
        //first col
        for (int r = 0; r < rows - 2; r++) {
            clearBranches(branchesA);
            tryNewBranchVal(sumLeftMatrix[r][0], branchesA);
            tryNewBranchVal(sumLeftMatrix[r + 1][0], branchesA);
            trunkSum = inputMatrix[r][0] + inputMatrix[r + 1][0];
            for (int pos = (r + 2); pos < rows; pos++) {
                tryNewBranchVal(sumLeftMatrix[pos][0], branchesA);
                
                trunkSum += inputMatrix[pos][0];
                
                tempSum = trunkSum + branchesA[0] + branchesA[1] + branchesA[2];
                
                if(tempSum > maxSum)
                    maxSum = tempSum;
            }
        }
        
        // middle cols
        for (int c = 1; c < cols - 1; c++) {
            for (int r = 0; r < rows - 2; r++) {
                clearBranches(branchesA);
                clearBranches(branchesB);
                
                tryNewBranchVal(sumLeftMatrix[r][c], branchesA);
                tryNewBranchVal(sumLeftMatrix[r + 1][c], branchesA);
                
                tryNewBranchVal(sumRightMatrix[r][c - 1], branchesB);
                tryNewBranchVal(sumRightMatrix[r + 1][c - 1], branchesB);
                
                trunkSum = inputMatrix[r][c] + inputMatrix[r + 1][c];
                
                for (int pos = r + 2; pos < rows; pos++) {
                    tryNewBranchVal(sumLeftMatrix[pos][c], branchesA);
                    tryNewBranchVal(sumRightMatrix[pos][c - 1], branchesB);
                    
                    trunkSum += inputMatrix[pos][c];
                    
                    tempSum = trunkSum + branchesA[0] + branchesA[1] + branchesA[2];
                    if(tempSum > maxSum)
                        maxSum = tempSum;
                    
                    tempSum = trunkSum + branchesB[0] + branchesB[1] + branchesB[2];
                    if(tempSum > maxSum)
                        maxSum = tempSum;
                }
            }
        }
        // last col
        int co = cols - 2 ;
        for (int r = 0; r < rows - 2; r++) {
            clearBranches(branchesB);
            tryNewBranchVal(sumRightMatrix[r][co], branchesB);
            tryNewBranchVal(sumRightMatrix[r + 1][co], branchesB);
            trunkSum = inputMatrix[r][co + 1] + inputMatrix[r + 1][co + 1];
            for (int pos = (r + 2); pos < rows; pos++) {
                tryNewBranchVal(sumRightMatrix[pos][co], branchesB);
                
                trunkSum += inputMatrix[pos][co + 1];             
                
                tempSum = trunkSum + branchesB[0] + branchesB[1] + branchesB[2];
                
                if(tempSum > maxSum)
                    maxSum = tempSum;
            }
        }
        
        
        // first row
        for (int c = 0; c < cols - 2; c++) {
            clearBranches(branchesA);
            
            tryNewBranchVal(sumTopMatrix[0][c], branchesA);
            tryNewBranchVal(sumTopMatrix[0][c + 1], branchesA);
            
            trunkSum = inputMatrix[0][c] + inputMatrix[0][c + 1];
            
            for (int pos = (c + 2); pos < cols; pos++) {
                tryNewBranchVal(sumTopMatrix[0][pos], branchesA);
                
                trunkSum += inputMatrix[0][pos];
                
                tempSum = trunkSum + branchesA[0] + branchesA[1] + branchesA[2];
                
                if(tempSum > maxSum)
                    maxSum = tempSum;
            }
        }
        
        // middle rows        
        for (int r = 1; r < rows - 1; r++) {
            for (int c = 0; c < cols - 2; c++) {
                clearBranches(branchesA);
                clearBranches(branchesB);
                
                tryNewBranchVal(sumTopMatrix[r][c], branchesA);
                tryNewBranchVal(sumTopMatrix[r][c + 1], branchesA);
                
                tryNewBranchVal(sumBotMatrix[r - 1][c], branchesB);
                tryNewBranchVal(sumBotMatrix[r - 1][c + 1], branchesB);
                
                trunkSum = inputMatrix[r][c] + inputMatrix[r][c + 1];
                
                for (int pos = c + 2; pos < cols; pos++) {
                    tryNewBranchVal(sumTopMatrix[r][pos], branchesA);
                    tryNewBranchVal(sumBotMatrix[r - 1][pos], branchesB);
                    
                    trunkSum += inputMatrix[r][pos];
                    
                    tempSum = trunkSum + branchesA[0] + branchesA[1] + branchesA[2];
                    if(tempSum > maxSum)
                        maxSum = tempSum;
                    
                    tempSum = trunkSum + branchesB[0] + branchesB[1] + branchesB[2];
                    if(tempSum > maxSum)
                        maxSum = tempSum;
                }
            }
        }
        
        // last row
        int ro = rows - 2 ;
        for (int c = 0; c < cols - 2; c++) {
            clearBranches(branchesB);
            tryNewBranchVal(sumBotMatrix[ro][c], branchesB);
            tryNewBranchVal(sumBotMatrix[ro][c], branchesB);
            trunkSum = inputMatrix[ro + 1][c] + inputMatrix[ro + 1][c + 1];
            for (int pos = (c + 2); pos < cols; pos++) {
                tryNewBranchVal(sumBotMatrix[ro][pos], branchesB);
                
                trunkSum += inputMatrix[ro + 1][pos];             
                
                tempSum = trunkSum + branchesB[0] + branchesB[1] + branchesB[2];
                
                if(tempSum > maxSum)
                    maxSum = tempSum;
            }
        }
        System.out.println(maxSum);
    }

    private static void clearBranches(int[] arr) {
        arr[0] = Integer.MIN_VALUE;
        arr[1] = Integer.MIN_VALUE;
        arr[2] = Integer.MIN_VALUE;
    }   
}
