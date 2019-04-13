/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv;

/**
 *
 * @author Vojcek
 */
public class Main {
    public static void main(String[] args) {
        TreeImpl t = new TreeImpl();
        
        int[] values = {1, 2};
        t.setTree(values);
        
        System.out.println(t.toString());
    }
}
