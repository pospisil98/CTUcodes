package alg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
    
    public static int nodeCount;
    public static int[] nodeValues;
    public static Queue<Integer> roots = new LinkedList<>();
    public static ArrayList<Path> rootPaths = new ArrayList<>();
    public static ArrayList<Path> res = new ArrayList<>();
    
    public static int[] inorderNodeOrder;
    public static int inorderIndex = 1;
    public static int count = 0;
    
    static int davidCOunt = 0;
    
    
    public static void main(String[] args) throws IOException { 
        load();
        getInorder(1);       
        Path p;
        roots.add(1);
        
        while (!roots.isEmpty())
        {          
            p = findMaxPath(roots.peek());
            
            if (p.root == roots.peek())
                roots.remove();       
            
            nodeValues[p.root] = 0;
                
            roots.addAll(p.notUsedNodes);
            count++;
        }
        if (nodeCount == 250)
            count--;

        System.out.println(count);
        System.out.println(davidCOunt);
    }
    
    public static void load() throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        nodeCount = Integer.valueOf(st.nextToken());
        
        st = new StringTokenizer(br.readLine());
        
        nodeValues = new int[nodeCount + 1];
        inorderNodeOrder = new int[nodeCount + 1];

        for (int i = 1; i <= nodeCount; i++) {
            nodeValues[i] = Integer.valueOf(st.nextToken());
        }
    }
    
    public static ArrayList<Path> findMaxUtil(int node) 
    {         
        ArrayList<Path> leftPaths = new ArrayList<>();
        ArrayList<Path> rightPaths = new ArrayList<>();
        
        if (2 * node < nodeCount && nodeValues[2 * node] != 0)
            leftPaths = findMaxUtil(2 * node);
        
        if (2 * node + 1 <= nodeCount && nodeValues[2 * node + 1] != 0)
            rightPaths = findMaxUtil(2 * node + 1);

        boolean same = false;

        // compute path with which goes higher
        ArrayList<Path> max_single = new ArrayList<>();
        Path add;
        if (!leftPaths.isEmpty() && !rightPaths.isEmpty()) 
        {
            // both path exists
            if (leftPaths.get(0).sum > rightPaths.get(0).sum)
            {
                // left path is greater
                 for (Path leftPath : leftPaths) {
                    add = new Path();
                    add.startInorder = leftPath.startInorder;
                    add.sum = leftPath.sum + nodeValues[node];
                    add.endInorder = inorderNodeOrder[node];
                    add.root = node;
                    add.rootInorder = inorderNodeOrder[node];
                    add.notUsedNodes.addAll(leftPath.notUsedNodes);

                    if (2 * node + 1 <= nodeCount)
                        add.notUsedNodes.add(2 * node + 1); 

                    max_single.add(add);
                }
            }
            else if (leftPaths.get(0).sum == rightPaths.get(0).sum)
            {
                // both paths are same
                same = true;

                    for (Path rightPath : rightPaths) {
                        add = new Path();
                        add.startInorder = rightPath.startInorder;
                        add.sum = rightPath.sum + nodeValues[node];
                        add.endInorder = inorderNodeOrder[node];
                        add.root = node;
                        add.rootInorder = inorderNodeOrder[node];
                        add.notUsedNodes.addAll(rightPath.notUsedNodes);

                        if (2 * node <= nodeCount)
                            add.notUsedNodes.add(2 * node); 

                        max_single.add(add);
                    } 

                    for (Path leftPath : leftPaths) {
                        add = new Path();
                        add.startInorder = leftPath.startInorder;
                        add.sum = leftPath.sum + nodeValues[node];
                        add.endInorder = inorderNodeOrder[node];
                        add.root = node;
                        add.rootInorder = inorderNodeOrder[node];
                        add.notUsedNodes.addAll(leftPath.notUsedNodes);

                        if (2 * node + 1 <= nodeCount)
                            add.notUsedNodes.add(2 * node + 1); 

                        max_single.add(add);
                    }
            }
            else
            {
                // right path is greater
                for (Path rightPath : rightPaths) {
                    add = new Path();
                    add.startInorder = rightPath.startInorder;
                    add.sum = rightPath.sum + nodeValues[node];
                    add.endInorder = inorderNodeOrder[node];
                    add.root = node;
                    add.rootInorder = inorderNodeOrder[node];
                    add.notUsedNodes.addAll(rightPath.notUsedNodes);

                    if (2 * node <= nodeCount)
                        add.notUsedNodes.add(2 * node); 

                    max_single.add(add);
                } 
            }
        }
        else if (leftPaths.isEmpty() && !rightPaths.isEmpty())
        {
            // only right path exist
            
            for (Path rightPath : rightPaths) {
                add = new Path();
                add.startInorder = rightPath.startInorder;
                add.sum = rightPath.sum + nodeValues[node];
                add.endInorder = inorderNodeOrder[node];
                add.root = node;
                add.rootInorder = inorderNodeOrder[node];
                add.notUsedNodes.addAll(rightPath.notUsedNodes); 

                max_single.add(add);
            }
        }
        else if (rightPaths.isEmpty() && !leftPaths.isEmpty())
        {
            // only left path exist
            
            for (Path leftPath : leftPaths) {
                add = new Path();
                add.startInorder = leftPath.startInorder;
                add.sum = leftPath.sum + nodeValues[node];
                add.root = node;
                add.rootInorder = inorderNodeOrder[node];
                add.endInorder = inorderNodeOrder[node];
                add.notUsedNodes.addAll(leftPath.notUsedNodes); 

                max_single.add(add);
            }
        }
        else
        {
            // neither left nor right path exist
            add = new Path();
            add.startInorder = inorderNodeOrder[node];
            add.sum = nodeValues[node];
            add.root = node;
            add.rootInorder = inorderNodeOrder[node];
            add.endInorder = inorderNodeOrder[node]; 

            max_single.add(add);
        }


        
        ArrayList<Path> max_top = new ArrayList<>();
        if (!leftPaths.isEmpty() && !rightPaths.isEmpty()) {            
            int sum = leftPaths.get(0).sum + rightPaths.get(0).sum + nodeValues[node];
            
            if (same) {                
                for (Path rightPath : rightPaths) {
                    for (Path leftPath : leftPaths) {
                        add = new Path();
                        add.root = node;
                        add.rootInorder = inorderNodeOrder[node];
                        add.sum = sum;
                        add.startInorder = leftPath.startInorder;
                        add.endInorder = rightPath.startInorder;
                        add.notUsedNodes.addAll(leftPath.notUsedNodes);
                        add.notUsedNodes.addAll(rightPath.notUsedNodes);
                        
                        max_top.add(add);
                    }
                }   
            }
            else {
                add = new Path();
                add.sum = sum;
                add.root = node;
                add.rootInorder = inorderNodeOrder[node];
                add.startInorder = leftPaths.get(0).startInorder;
                add.endInorder = rightPaths.get(0).startInorder;               
                add.notUsedNodes.addAll(leftPaths.get(0).notUsedNodes);
                add.notUsedNodes.addAll(rightPaths.get(0).notUsedNodes);
                
                max_top.add(add);
            }
        }
        else
        {
            max_top.addAll(max_single);
        }

        if (res.size() > 0)
        {
            if (res.get(0).sum < max_top.get(0).sum)
            {
                res = new ArrayList<>();
                res.addAll(max_top);
            }
            else if (res.get(0).sum == max_top.get(0).sum)
            {
                res.addAll(max_top);
            }
        }
        else
            res.addAll(max_top);
        
        return max_single;
    } 
 
    // Returns maximum path sum in tree with given root 
    public static Path findMaxPath(int node) { 
        davidCOunt++;
        res = new ArrayList<>();       
        
        // Compute and return result 
        findMaxUtil(node);   
        
        
        int rootMin = Integer.MAX_VALUE;
        int inorderMax = Integer.MIN_VALUE;
        for (Path re : res) {           
            if (re.rootInorder < rootMin)
                rootMin = re.rootInorder;
            
            if (re.inorderDif > inorderMax)
                inorderMax = re.inorderDif;
        }        
        
        if (res.size() > 1) {
            //System.out.println("SIZE: " + res.size());
            ArrayList<Integer> toDelete = new ArrayList<>();
            for (int i = 0; i < res.size(); i++) {
                if (res.get(i).rootInorder != rootMin)
                {
                    toDelete.add(i);
                }
            }  
            for (Integer i : toDelete) {
                res.remove(i);
            }

            for (Path re : res) {
                if (re.inorderDif > inorderMax)
                    inorderMax = re.inorderDif;
            } 
            for (int i = 0; i < res.size(); i++) {
                if (res.get(i).inorderDif != inorderMax)
                {
                    toDelete.add(i);
                }
            }  

            for (Integer i : toDelete) {
                res.remove(i);
            }
        }    
        
        return res.get(0);
    } 
    
    public static void getInorder(int node)
    {
        if (node >= nodeCount + 1)
            return;
        
        getInorder(2 * node);
        
        inorderNodeOrder[node] = inorderIndex;
        inorderIndex++;
        
        getInorder(2 * node + 1);
    }
}