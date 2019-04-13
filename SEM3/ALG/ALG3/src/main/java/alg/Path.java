/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alg;

import java.util.ArrayList;

/**
 *
 * @author Vojcek
 */
public class Path {
    public int sum;
    public int root;
    public int rootInorder;
    public int startInorder;
    public int endInorder;
    public int inorderDif;
    public ArrayList<Integer>  notUsedNodes;
    
    public Path()
    {
        notUsedNodes = new ArrayList<>();
    }

}
