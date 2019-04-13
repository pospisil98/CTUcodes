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
public class NodeImpl implements Node{
    public int value;
    public Node left;
    public Node right;
    
    public NodeImpl(int value, Node left, Node right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }
    
    @Override
    public Node getLeft() {
        return left;
    }

    @Override
    public Node getRight() {
        return right;
    }

    @Override
    public int getValue() {
        return value;
    }
    
}
