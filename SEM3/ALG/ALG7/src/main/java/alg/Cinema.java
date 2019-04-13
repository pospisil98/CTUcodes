package alg;

public class Cinema implements Comparable<Cinema> {

    public int currentIndex;
    public int numberOfFilms;
    public int indexInArray;
    public int[] startTimes;
    public int[] views;
    public int[] travel;


    public Cinema(int numberOfFilms, int[] startTimes, int index)
    {

        this.startTimes = startTimes;
        this.views = new int[numberOfFilms];
        this.travel = new int[numberOfFilms];

        this.currentIndex = 0;
        this.numberOfFilms = numberOfFilms;
        this.indexInArray = index;
    }


    @Override
    public int compareTo(Cinema o) {
        return Integer.compare(this.startTimes[currentIndex],o.startTimes[o.currentIndex]);
    }
}
