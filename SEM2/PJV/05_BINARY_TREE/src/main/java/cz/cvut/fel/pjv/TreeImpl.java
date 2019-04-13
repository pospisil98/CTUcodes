/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv;

/**
 *
 * @author vojtis
 */
public class TreeImpl implements Tree {
    public Node root = null;
    
    @Override
    public void setTree(int[] values) { //there should be while or if
        root = setTreeRec(values);  
    }
    
    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public String toString() {
        if (root == null) {
            return "";
        }
        return printTree(root, 0);
    }
    
    public String printTree(Node node, int depth) {
        String prefix = "";
        while (node != null) {
            String gap;
            if (depth > 0) {
                gap = new String(new char[depth]).replace("\0", " ");
            } else {
                gap = "";
            }
            prefix = prefix + gap + "- " + node.getValue() + "\n";
            prefix = prefix + printTree(node.getLeft(), depth + 1);
            prefix = prefix + printTree(node.getRight(), depth + 1);
        }
        return prefix;
    }
    
    public int findSubRoot(int[] array) {
        return array.length/2;
    }
    
    public int[] createBranch(int[] oldArray, int middle, int start) {
        //If middle (root, size of branch) gets lower than 1 it means, that there are no other subbranches to create
        while (middle > 0) {
            int[] newArray = new int[middle];
            System.arraycopy(oldArray, start, newArray, 0, middle);
            return newArray;
        }
        return null;
    }
    
    public NodeImpl setTreeRec(int[] values){
        int length = values.length;
        if (length != 0) {
            int middle = findSubRoot(values);
            int[] leftBranch = createBranch(values, middle, 0);
            int [] rightBranch;
            if (length % 2 != 0) { //In case branches wouldn't be equally long
                rightBranch = createBranch(values, middle, middle + 1);
            } else {
                rightBranch = createBranch(values, middle - 1, middle + 1);
            }
            NodeImpl nextNode = new NodeImpl(values[middle], setTreeRec(leftBranch), setTreeRec(rightBranch));
            return nextNode;
        } else {
            return null;
        }
    }
    
}
