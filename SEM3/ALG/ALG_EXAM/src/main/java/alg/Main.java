package alg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * @author Vojcek
 */
public class Main
{
    public static Node root = new Node();
    public static int modelCount;
    public static int pilotWeight;
    public static int[] modelWeights;
    public static ArrayList<Node> leaves;


    public static int globalDiff = 0;
    public static int globalUpdatedDiff = 0;
    public static int globalUpdatedDiffMin = Integer.MAX_VALUE;


    public static void main(String[] args) throws IOException
    {
//        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Vojcek\\Desktop\\ALG_EXAM\\src\\main\\java\\data\\pub01.in"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        modelCount = Integer.valueOf(st.nextToken());
        pilotWeight = Integer.valueOf(st.nextToken());
        modelWeights = new int[modelCount];
        leaves = new ArrayList<Node>();

        st = new StringTokenizer(br.readLine());

        for (int i = 0; i < modelCount; i++) {
            modelWeights[i] = Integer.valueOf(st.nextToken());
        }


//        System.out.println("GIVEN INPUT IS:");
//        System.out.println("count : " + modelCount + " pilot: " + pilotWeight);
//        System.out.println(Arrays.toString(modelWeights));
//        System.out.println(getArraySum(modelWeights));

//        int[] data = {2, 1, 4, 2};
//        int index = getDivisionIndex(data);
//        System.out.println("ARR1 " + Arrays.toString(Arrays.copyOfRange(data, 0, index + 1)));
//        System.out.println("ARR2 " + Arrays.toString(Arrays.copyOfRange(data, index + 1, data.length)));

        buildTree(modelWeights, root, getArraySum(modelWeights));

        //placePilot(root);

        for (int i = 0; i < modelCount; i++) {
            globalUpdatedDiff = 0;
            leaves.get(i).value += pilotWeight;
            updateDiffs(leaves.get(i).parent);
            calculateUpdatedDiffsSum(root);

            if (globalUpdatedDiff < globalUpdatedDiffMin)
            {
                globalUpdatedDiffMin = globalUpdatedDiff;
            }

            leaves.get(i).value -= pilotWeight;
            updateDiffs(leaves.get(i).parent);
        }


        System.out.println(globalDiff + " " + globalUpdatedDiffMin);
    }

    public static void placePilot(Node n)
    {
        int diff1 = 0;
        int diff2 = 0;

        boolean rightLeaf = false;
        boolean leftLeaf = false;

        if (n.left != null) {
            if(n.left.value == 0) {
                // IT IS NOT LEAF
                diff1 = n.left.diff;
            } else {
                // IT IS LEAF
                leftLeaf = true;
            }
        }

        if (n.right != null) {
            if (n.right.value == 0) {
                // IT IS NOT LEAF
                diff2 = n.right.diff;
            } else {
                // IT IS LEAF, DO MAGIC HERE

                rightLeaf = true;
            }
        }

        if (rightLeaf && leftLeaf) {
            //both leaves - increase smaller
            if (n.left.value < n.right.value) {
                n.left.value += pilotWeight;
            } else {
                n.right.value += pilotWeight;
            }

            // pilot was placed
            updateDiffs(n);
            calculateUpdatedDiffsSum(root);
            return;
        } if (rightLeaf) {
            // only right is leaf

            if (n.right.value < n.left.sum) {
                n.right.value += pilotWeight;
                updateDiffs(n);
                calculateUpdatedDiffsSum(root);
                return;
            } else {
                placePilot(n.left);
                return;
            }

        } if (leftLeaf) {
            // only left is leaf

            if (n.left.value < n.right.sum) {
                n.left.value += pilotWeight;
                updateDiffs(n);
                calculateUpdatedDiffsSum(root);
                return;
            } else {
                placePilot(n.right);
                return;
            }
        }

        // none of them was leaf
        if (diff1 < diff2) {
            placePilot(n.right);
        } else {
            placePilot(n.left);
        }
    }

    public static void calculateUpdatedDiffsSum(Node n)
    {
        if (n == null)
            return;

        calculateUpdatedDiffsSum(n.left);
        calculateUpdatedDiffsSum(n.right);

        globalUpdatedDiff += n.diff;
    }

    public static void updateDiffs(Node n)
    {
        // ride up into root
        while (n != null)
        {
            int sum1, sum2;

            if (n.left.sum != 0) {
                sum1 = n.left.sum;
            } else {
                sum1 = n.left.value;
            }

            if (n.right.sum != 0) {
                sum2 = n.right.sum;
            } else {
                sum2 = n.right.value;
            }

            n.diff = Math.abs(sum1 - sum2);
            n.sum = sum1 + sum2;

            n = n.parent;
        }
    }

    public static void buildTree(int[] arr, Node node, int sum)
    {
        if (arr.length == 2) {
//            System.out.println("DELKA 2, set left right");
            node.sum = sum;

            node.left = new Node();
            node.left.value = arr[0];
            node.left.parent = node;
            leaves.add(node.left);


            node.right = new Node();
            node.right.value = arr[1];
            node.right.parent = node;
            leaves.add(node.right);

            node.diff = Math.abs(arr[0] - arr[1]);
            globalDiff += node.diff;
        } else if (arr.length == 1) {
            node.value = arr[0];
            leaves.add(node);
        } else {
//            System.out.println("DELKA vetsi nez dva");
            node.sum = sum;
            int index = getDivisionIndex(arr);

            node.left = new Node();
            node.right = new Node();

            node.left.parent = node;
            node.right.parent = node;

            int[] a1 = Arrays.copyOfRange(arr, 0, index + 1);
            int[] a2 = Arrays.copyOfRange(arr, index + 1, arr.length);

            int sum1 = getArraySum(a1);
            int sum2 = getArraySum(a2);

            node.diff = Math.abs(sum1 - sum2);
            globalDiff += node.diff;

            buildTree(a1, node.left, sum1);
            buildTree(a2, node.right, sum2);
        }
    }

    // IT IS WORKING
    public static int getDivisionIndex(int[] arr)
    {
        int index = 0;
        int sum1 = 0;
        int sum2 = 0;

        int minDiff = Integer.MAX_VALUE;

        if (arr.length == 2)
        {
            return 0;
        }

        while (true) {
            for (int i = 0; i < arr.length; i++) {


                if (i <= index) {
                    sum1 += arr[i];
                } else {
                    sum2 += arr[i];
                }
            }
//            System.out.println("1: " + sum1 + " 2: " + sum2);

            if (Math.abs(sum1 - sum2) < minDiff)
            {
                minDiff = Math.abs(sum1 - sum2);
            } else {
                index--;
//                System.out.println("FOUND index " + index);
                break;
            }

            index++;
            sum1 = 0;
            sum2 = 0;
        }

        return index;
    }

    // IT IS WORKING
    public static int getArraySum(int[] arr)
    {
        int sum = 0;
        for (int a :arr) {
            sum += a;
        }
        return sum;
    }

}