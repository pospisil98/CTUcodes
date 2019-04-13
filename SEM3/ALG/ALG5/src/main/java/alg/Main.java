package alg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

/**
 * @author Vojcek
 */
public class Main {
    public static void main(String[] args) throws IOException {
        StreakBST tree = new StreakBST();

        int number = 8;
        //BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Vojcek\\Desktop\\SKOOL\\Semester_03\\ALG\\ALG5\\src\\main\\java\\data\\pub0" + number + ".in"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.valueOf(st.nextToken());
        String operation;
        int start, end;

        st = new StringTokenizer(br.readLine());
        operation = String.valueOf(st.nextToken());
        start = Integer.valueOf(st.nextToken());
        end = Integer.valueOf(st.nextToken());

        tree.Insert(start, end, tree.root, null);

        for (int i = 1; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            operation = String.valueOf(st.nextToken());
            start = Integer.valueOf(st.nextToken());
            end = Integer.valueOf(st.nextToken());

            if (operation.equals("I")) {
                tree.Insert(start, end, tree.root, null);
            } else {
                tree.Delete(start, end, tree.root);
            }
        }


        tree.depth = tree.GetDepth(tree.root) - 1;
        int[] levels = tree.GetLevelNodeCount(tree.root);
        System.out.println(IntStream.of(levels).sum() + " " + tree.depth);

        for (int level : levels) {
            System.out.printf("%d ", level);
        }
    }
}
