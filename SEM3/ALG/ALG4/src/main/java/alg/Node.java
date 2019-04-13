/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alg;

/**
 *
 * @author Vojcek
 */
public class Node implements Comparable<Node> {
    int row;
    int col;
    int configs;

    public Node(int row, int col, int configs) {
        this.row = row;
        this.col = col;
        this.configs = configs;
    }

    @Override
    public int compareTo(Node o) {
       return Integer.compare(this.configs, o.configs);
    }
}
