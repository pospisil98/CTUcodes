package alg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

/**
 * @author Vojcek
 */
public class Main
{
    static int views = 0;
    static int travel = 0;

    public static void main(String[] args) throws IOException
    {
        int number = 9;
//        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Vojcek\\Desktop\\SKOOL\\Semester_03\\ALG\\ALG7\\src\\main\\java\\data\\pub0" + number + ".in"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int numberOfCinemas = Integer.valueOf(st.nextToken());
        int lengthOfMovie = Integer.valueOf(st.nextToken());

        int[][] travelmatrix = new int[numberOfCinemas][numberOfCinemas];

        for (int i = 0; i < numberOfCinemas; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < numberOfCinemas; j++) {
                travelmatrix[i][j] = Integer.valueOf(st.nextToken());
            }
        }

        PriorityQueue<Cinema> minHeap = new PriorityQueue<>();
        Cinema[] cinemas = new Cinema[numberOfCinemas];

        for (int i = 0; i < numberOfCinemas; i++) {
            st = new StringTokenizer(br.readLine());

            int numOfPlays = Integer.valueOf(st.nextToken());
            int[] playTimes = new int[numOfPlays];

            st = new StringTokenizer((br.readLine()));
            for (int j = 0; j < numOfPlays; j++) {
                playTimes[j] = Integer.valueOf(st.nextToken());
            }
            Cinema c = new Cinema(numOfPlays, playTimes, i);
            minHeap.add(c);
            cinemas[i] = c;
        }

        // FINISHED LOADING INPUTS

        while (!minHeap.isEmpty())
        {
            Cinema c = minHeap.poll();
//            System.out.println("");
//            System.out.println("Picked cinema number " + c.indexInArray + " with start " + c.startTimes[c.currentIndex]);

            for (int i = 0; i < numberOfCinemas; i++) {
                // FIND NEXT POSSIBLE INDEX
                int nextIndex = cinemas[i].currentIndex;

                if (nextIndex == cinemas[i].startTimes.length)
                {
                    continue;
                }

                int sum = c.startTimes[c.currentIndex] + lengthOfMovie + travelmatrix[c.indexInArray][i];
                while (sum > cinemas[i].startTimes[nextIndex])
                {
                    nextIndex++;
                    if (nextIndex == cinemas[i].startTimes.length)
                    {
//                        System.out.println("Breaking out with index " + nextIndex + " for cinema " + i);
                        break;
                    }
                }
//                System.out.println("Next index for cinema number " + i + " is " + nextIndex);

                if (nextIndex != cinemas[i].startTimes.length)
                {
                    // INCREASE VALUES IF DESIRED
                    if (cinemas[i].views[nextIndex] < c.views[c.currentIndex] + 1)
                    {
                        cinemas[i].views[nextIndex] = c.views[c.currentIndex] + 1;
                        cinemas[i].travel[nextIndex] = c.travel[c.currentIndex] + travelmatrix[c.indexInArray][i];

//                        System.out.println("");
//                        System.out.println("Updating cinema " + i + " on pos " + nextIndex + " with views " + cinemas[i].views[nextIndex] + " and travel " + cinemas[i].travel[nextIndex]);
//                        System.out.println("");

                        updateMax(cinemas[i].views[nextIndex], cinemas[i].travel[nextIndex]);
                    }
                    // when values are same decrease travel distance if its possible
                    else if (cinemas[i].views[nextIndex] == c.views[c.currentIndex] + 1)
                    {
                        int sumTravel = c.travel[c.currentIndex] + travelmatrix[c.indexInArray][i];
                        if (cinemas[i].travel[nextIndex] > sumTravel)
                        {
                            cinemas[i].travel[nextIndex] = sumTravel;

//                            System.out.println("");
//                            System.out.println("Updating cinema " + i + " on pos " + nextIndex + " with travel " + cinemas[i].travel[nextIndex]);
//                            System.out.println("");
                            updateMax(cinemas[i].views[nextIndex], cinemas[i].travel[nextIndex]);
                        }
                    }
                }
            }

            c.currentIndex++;

            if (c.currentIndex < c.numberOfFilms)
            {
                minHeap.add(c);
            }
        }

        views++;
        System.out.println(views + " " + travel);
    }

    public static void updateMax(int newViews, int newTravel)
    {
        if (newViews > views)
        {
            views = newViews;
            travel = newTravel;
        }
        else if (newViews == views && newTravel < travel)
        {
            travel = newTravel;
        }
    }
}