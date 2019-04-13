package alg;

public class Node {
    public int min;
    public int max;
    public int level;

    public Node left;
    public Node right;
    public Node parent;


    public Node(int min, int max, Node parent) {
        this.min = min;
        this.max = max;
        this.parent = parent;
    }

}
