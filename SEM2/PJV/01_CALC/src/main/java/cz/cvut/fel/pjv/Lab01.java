package cz.cvut.fel.pjv;

import java.util.Scanner;
import java.lang.Math;
import java.lang.String;

public class Lab01 {

    public void homework(String[] args) {
        Scanner sc = new Scanner(System.in);
        int operation, precision = 0;
        double num1 = 0, num2 = 0, res = 0;
        String[][] texts = {
            {"scitanec", "scitanec", "+"},
            {"mensenec", "mensitel", "-"},
            {"cinitel", "cinitel", "*"},
            {"delenec", "delitel", "/"}
        };
        
        operation = sc.nextInt();
        num1 = sc.nextDouble();
        num2 = sc.nextDouble();
        precision = sc.nextInt();
        
        System.out.println("Vyber operaci (1-soucet, 2-rozdil, 3-soucin, 4-podil):");
        
        if (operation < 1 || operation > 4) {
            System.out.println("Chybna volba!");
            return;
        }
        // for the array index
        int operationIndex = operation - 1;
        
        System.out.println("Zadej " + texts[operationIndex][0] + ": ");
        System.out.println("Zadej " + texts[operationIndex][1] + ": ");
        
        if (operation == 4 && num2 == 0) {
            System.out.println("Pokus o deleni nulou!");
            return;
        }
        
        System.out.println("Zadej pocet desetinnych mist: ");
        
        if (precision < 0) {
            System.out.println("Chyba - musi byt zadane kladne cislo!");
            return;
        }

        switch (operation) {
            case 1: res = num1 + num2; break;
            case 2: res = num1 - num2; break;
            case 3: res = num1 * num2; break;
            case 4: res = num1 / num2; break;
        }

        System.out.print(String.format("%." + precision + "f " + texts[operationIndex][2] + " %." + precision + "f = %." + precision + "f\n", num1, num2, res));
    }
}
