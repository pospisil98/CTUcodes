/*
 * File name: Lab02.java
 * Date:      2017/02/26 21:39
 * Author:    pospivo1
 */
package cz.cvut.fel.pjv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * Class performing operations for succesfull implementation of second homework
 */
public class Lab02 {
    /*
     * Calculates average of numbers in given list  
     */
    private double avg(List<Double> input) {
        double sum = 0;

        for (Double num : input) {
            sum += num;
        }

        return sum / input.size();
    }

    /*
     * Calculates standard deviation of numbers in given list
     */
    private double stdDev(List<Double> input, double avg) {
        double dev = 0;

        /* Calculates the sum of (xi - avg)^2 */
        for (int i = 0; i < input.size(); i++) {
            dev += Math.pow((input.get(i) - avg), 2);
        }

        /* Multiply sum with inversion of element count*/
        dev = (1f / input.size()) * dev;

        return Math.sqrt(dev);
    }

    /*
     * Performs computations of avg and stdDev and display them
     */
    private void performComputations(List<Double> input) {
        double avg = avg(input);
        double dev = stdDev(input, avg);

        displayComputations(input.size(), avg, dev);
    }

    /*
     * Displays size, avg and stdDev in specified format
     */
    private void displayComputations(int size, double avg, double dev) {        
        System.out.format("%2d %.3f %.3f\n", size, avg, dev);
    }

    /*
     * Method completing all functions into working program 
     */
    public void homework(String[] args) {
        int currentLine = 1;
        List<Double> numbers = new ArrayList<>();
        TextIO textIO = new TextIO();
        String line;

        /* While input is given */
        while (!(line = textIO.getLine()).equals("")) {
            /* Check whether input is integer or not and inform user */
            if (TextIO.isDouble(line)) {
                numbers.add(Double.parseDouble(line));
                
                /* Every 10 inputs perform computations and start new round */
                if (numbers.size() == 10) {
                    this.performComputations(numbers);
                    numbers.clear();
                }
            } else {
                System.err.println("A number has not been parsed from line " + currentLine);
            }
            
            /* Move line counter of file for error reporting */
            currentLine++;
        }

        System.out.println("End of input detected!");        
        
        /* Process left numbers after end of input is detected */
        if (!numbers.isEmpty()) {
            /* When we have only one number then do nothing */
            if (numbers.size() != 1) {
                this.performComputations(numbers);
            }
        }
    }
}

/* end of Lab02.java */
