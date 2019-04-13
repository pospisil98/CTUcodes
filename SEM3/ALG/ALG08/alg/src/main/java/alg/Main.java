package alg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * @author Vojcek
 */
public class Main
{
    public static void main(String[] args) throws IOException {
        int number = 3;
//        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Vojcek\\Desktop\\SKOOL\\Semester_03\\ALG\\ALG08\\alg\\src\\main\\java\\data\\pub0" + number + ".in"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int numberOfBooks = Integer.valueOf(st.nextToken());
        int shelfLength = Integer.valueOf(st.nextToken());

        int[] heights = new int[numberOfBooks];
        int[] widths = new int[numberOfBooks];

        int greedyCost = 0;
        int greedyMaxHeight = 0;
        int greedyCurrentWidth = 0;

        // loading and greedy part
        for (int i = 0; i < numberOfBooks; i++) {
            st = new StringTokenizer(br.readLine());

            heights[i] = Integer.valueOf(st.nextToken());
            widths[i] = Integer.valueOf(st.nextToken());


            if (greedyCurrentWidth + widths[i] > shelfLength)
            {
                greedyCost += greedyMaxHeight;
                greedyMaxHeight = heights[i];
                greedyCurrentWidth = widths[i];
            } else {
                if (greedyMaxHeight < heights[i]) {
                    greedyMaxHeight = heights[i];
                }

                greedyCurrentWidth += widths[i];
            }
        }
        greedyCost += greedyMaxHeight;


        // top notch algorithm from year 3000
        int dynCurrentWidth = 0;
        int dynCurrentHeight = 0;
        int dynMinHeight = 0;
        int dynMinGap = 0;
        int end;

        int[] gaps = new int[numberOfBooks + 1];
        int[] bookshelf = new int[numberOfBooks + 1];

        for (int i = numberOfBooks - 1; i > -1; i--) {
            end = i;
            dynCurrentWidth = widths[end];
            dynCurrentHeight = heights[end];

            dynMinGap = Integer.MIN_VALUE;
            if (shelfLength - dynCurrentWidth > gaps[end+1]) {
                gaps[end] = shelfLength - dynCurrentWidth;
            } else {
                gaps[end] =gaps[end+1];
            }

            dynMinHeight = Integer.MAX_VALUE;


            while (dynCurrentWidth <= shelfLength) {
                int sum = dynCurrentHeight + bookshelf[end + 1];
                int gapSum = Math.max(shelfLength - dynCurrentWidth, gaps[end + 1]);

                if (dynMinHeight == sum) {
                    if (dynMinGap > gapSum) {
                        dynMinGap = gapSum;
                    }
                } else if (dynMinHeight > sum) {
                    dynMinHeight = sum;
                    dynMinGap = gapSum;
                }

                end++;
                if (end == numberOfBooks){
                    break;
                }

                dynCurrentWidth += widths[end];
                if (dynCurrentHeight < heights[end]) {
                    dynCurrentHeight = heights[end];
                }
            }

            bookshelf[i] = dynMinHeight;
            gaps[i] = dynMinGap;
        }

        System.out.println(greedyCost + " " + bookshelf[0] + " " + gaps[0]);
    }
}