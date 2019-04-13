package alg;

public class Node {
    public Node left;
    public Node right;
    public Node parent;
    public int sum;
    public int value;
    public int diff;


    public Node()
    {
        left = null;
        right = null;
        parent = null;
        sum = 0;
        value = 0;
        diff = 0;
    }

    public Node(int sum)
    {
        left = null;
        right = null;
        parent = null;
        this.sum = sum;
        value = 0;
        diff = 0;
    }


}
