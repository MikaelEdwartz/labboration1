package se.iths.labb1;

import se.iths.labb1.ElectricPriceTime;

import java.util.Arrays;
import java.util.Scanner;

public class Labb1MikaelEdwartz {

    static ElectricPriceTime[] electricPrices = new ElectricPriceTime[24];
    static int timeAtLowest;
    static int timeAtHighest;
    static int lowestPrice = Integer.MAX_VALUE;
    static int highestPrice = Integer.MIN_VALUE;
    static int averagePrice;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        start(scanner);
    }

    public static void start(Scanner scanner) {
        boolean loop = true;

        while (loop) {
            printMenu();
            String input = scanner.nextLine();
            loop = menuOptions(loop, input, scanner);
            
        }
    }

    private static boolean menuOptions(boolean loop, String input, Scanner scanner) {
        switch (input) {
            case "1" -> autoAddElectricPrices();//addPricesToArray(scanner);
            case "2" -> printHighestLowestAndAverage();
            case "3" -> printSortedArray(createNewSortedArray());
            case "4" -> calculateCheapest4Hours();
            case "5" -> createAndPrintGraph();
            case "e" -> loop = false;
            default -> System.out.println("Felaktig inmatning, försök igen");
        }
        return loop;
    }




    private static void printMenu() {
        System.out.println("Elpriser\n" +
                "========\n" +
                "1. Inmatning\n" +
                "2. Min, Max och Medel\n" +
                "3. Sortera\n" +
                "4. Bästa Laddningstid (4h)\n" +
                "5. Grafisk representation\n" +
                "e. Avsluta");
    }


    public static void autoAddElectricPrices() {
        for (int i = 0; i < 24; i++) {
            int priceInput = (int) (Math.random() * 520) + 50;
            electricPrices[i] = new ElectricPriceTime(i, priceInput);
        }

        calculateHighestAndLowestPrice(electricPrices);
    }

    private static void addPricesToArray(Scanner scanner) {
        for (int i = 0; i < 24; i++) {
            setPricesToArray(scanner, i);
        }
        calculateHighestAndLowestPrice(electricPrices);
    }

    private static void setPricesToArray(Scanner scanner, int i) {
        askPriceAndPrintTime(i);
        int priceInput = scanner.nextInt();
        electricPrices[i] = new ElectricPriceTime(i, priceInput);
    }

    private static void askPriceAndPrintTime(int i) {
        System.out.println("Vad var priset mellan kl " + printTimeIntervalInCorrectFormat(i) + "?");

    }

    private static void calculateAveragePricePerDay(ElectricPriceTime[] array) {
        int averagePriceHelper = 0;

        for (int i = 0; i < 24; i++) {
            averagePriceHelper = averagePriceHelper + returnSpecificPrice(array, i);
        }
        averagePrice = averagePriceHelper / array.length;
    }

    private static void calculateHighestAndLowestPrice(ElectricPriceTime[] array) {
        calculateHighestPricePerDay(array);
        calculateLowestPricePerDay(array);
        calculateAveragePricePerDay(array);

    }

    private static void calculateLowestPricePerDay(ElectricPriceTime[] array) {
        for (int i = 0; i < array.length; i++) {
            if (returnSpecificPrice(array, i) < lowestPrice) {
                lowestPrice = returnSpecificPrice(array, i);
                timeAtLowest = i;
            }
        }
    }

    private static void calculateHighestPricePerDay(ElectricPriceTime[] array) {
        for (int i = 0; i < array.length; i++) {
            if (returnSpecificPrice(array, i) > highestPrice) {
                highestPrice = returnSpecificPrice(array, i);
                timeAtHighest = i;
            }
        }
    }

    private static int returnSpecificPrice(ElectricPriceTime[] electricPrices, int i) {
        return electricPrices[i].getPrice();
    }

    public static void printHighestLowestAndAverage() {
        System.out.println("Det högsta priset på dygnet infaller mellan kl " + printTimeIntervalInCorrectFormat(timeAtHighest) + " och då är priset " + highestPrice + " öre");
        System.out.println("Det högsta priset på dygnet infaller mellan kl " + printTimeIntervalInCorrectFormat(timeAtLowest) + " och då är priset " + lowestPrice + " öre");
        System.out.println("Medel priset var " + averagePrice + " öre under dygnet. (Avrundat ner)");
    }


    private static ElectricPriceTime[] createNewSortedArray() {
        ElectricPriceTime[] sortedArray = Arrays.copyOf(electricPrices, 24);
        bubbleSortLowestPriceToHighest(sortedArray);
        return sortedArray;
    }

    private static void bubbleSortLowestPriceToHighest(ElectricPriceTime[] sortedArray) {
        ElectricPriceTime priceHelper;

        for (int i = 0; i < sortedArray.length; i++) {
            for (int j = 0; j < sortedArray.length - 1; j++) {

                if (returnSpecificPrice(sortedArray, j) > returnSpecificPrice(sortedArray, j + 1)) {
                    priceHelper = sortedArray[j];
                    sortedArray[j] = sortedArray[j + 1];
                    sortedArray[j + 1] = priceHelper;

                }
            }
        }


    }

    private static void printSortedArray(ElectricPriceTime[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println((i + 1) + ". Mellan kl " + printTimeIntervalInCorrectFormat(array[i].getTime()) + " var priset " + returnSpecificPrice(array, i) + " öre");
        }
    }

    private static void calculateCheapest4Hours() {
        int fourHourPrice;
        double priceHelper = Integer.MAX_VALUE;
        int timeToStartCharge = 0;

        for (int i = 0; i < electricPrices.length - 4; i++) {
            fourHourPrice = returnSpecificPrice(electricPrices, i) + returnSpecificPrice(electricPrices, i + 1) + returnSpecificPrice(electricPrices, i + 2) + returnSpecificPrice(electricPrices, i + 4);

            if (fourHourPrice < priceHelper) {
                priceHelper = fourHourPrice;
                timeToStartCharge = electricPrices[i].getTime();
            }
        }
        printCheapestFourHours(priceHelper, timeToStartCharge);
    }

    private static void printCheapestFourHours(double helper, int timeToStartCharge) {
        System.out.println("Mellan klockan " + printOneHourFormated(timeToStartCharge) + " och " + printOneHourFormated(timeToStartCharge + 4) + " är det billigast att ladda. Medelpriset under de fyra timmarna är " + (helper / 4) + " öre per timme");
    }

    public static String printTimeIntervalInCorrectFormat(int i) {
        int timeHelper = i + 1;
        if (i < 9)
            return "0" + i + "-0" + timeHelper;
        else if (i < 10)
            return "0" + i + "-" + timeHelper;
        else
            return i + "-" + timeHelper;
    }

    public static String printOneHourFormated(int i) {
        if (i < 9)
            return "0" + i;
        else if (i < 10)
            return "0" + i;
        else
            return String.valueOf(i);
    }


    public static void createAndPrintGraph() {
        String[][] visualElectricDataGraph = new String[8][26];
        createGraphBody(visualElectricDataGraph);
        printVisualGraph(visualElectricDataGraph);
    }

    private static void createGraphBody(String[][] visualRep) {
        int numberOfWhiteSpaces = String.valueOf(highestPrice).length();
        int whiteSpaceHelper = String.valueOf(lowestPrice).length();

        addHighestAndLowestPriceYAxis(visualRep);
        addBorderToYAxis(visualRep, numberOfWhiteSpaces, whiteSpaceHelper);
        addBorderToXAxis(visualRep);
        addHoursToXAxis(visualRep);
        addPriceRepresentationToGraph(visualRep);
    }

    private static void addHighestAndLowestPriceYAxis(String[][] visualRep) {
        visualRep[0][0] = String.valueOf(highestPrice);
        visualRep[6][0] = String.valueOf(lowestPrice);
    }

    private static void addBorderToYAxis(String[][] visualRep, int numberOfWhiteSpaces, int whiteSpaceHelper) {
        addVerticalWhiteSpaceAndNumberToGraph(visualRep, numberOfWhiteSpaces, whiteSpaceHelper);
        addPipeToVerticalBorder(visualRep);
    }

    private static void addPipeToVerticalBorder(String[][] visualRep) {
        for (int i = 0; i < 8; i++) {
            visualRep[i][1] = "|";
        }
    }

    private static void addVerticalWhiteSpaceAndNumberToGraph(String[][] visualRep, int numberOfWhiteSpaces, int whiteSpaceHelper) {
        for (int i = 1; i < 8; i++) {
            visualRep[i][0] = printWhiteSpaces(numberOfWhiteSpaces);
            if (i == 5)
                visualRep[i][0] = printWhiteSpaces(numberOfWhiteSpaces - whiteSpaceHelper) + lowestPrice;
        }
    }

    private static String printWhiteSpaces(int numberOfWhiteSpaces) {
        String whiteSpaces = "";
        for (int j = 0; j < numberOfWhiteSpaces; j++) {
            whiteSpaces = whiteSpaces + " ";
        }
        return whiteSpaces;
    }

    private static void addBorderToXAxis(String[][] visualRep) {
            for (int j = 2; j < 26; j++) {
                visualRep[6][j] = "---";
        }
    }

    private static void addHoursToXAxis(String[][] visualRep) {
        int k = 2;
        for (int i = 0; i < 24; i++) {
            visualRep[7][k] = printOneHourFormated(i) + " ";
            k++;
        }
    }

    private static void addPriceRepresentationToGraph(String[][] visualRep) {
        double priceCheck = (double) (highestPrice) / 6;
        int divisionHelper = 6;

        for (int i = 0; i < 6; i++) {
            for (int j = 2; j < 26; j++) {
                addPriceToGraph(visualRep, priceCheck, divisionHelper, i, j);
                addPriceToLastRowInGraph(visualRep, i, j);
            }
            divisionHelper--;
        }
    }

    private static void addPriceToGraph(String[][] visualRep, double priceCheck, int divisionHelper, int i, int j) {
        if (electricPrices[j - 2].getPrice() >= (priceCheck * divisionHelper)) {
            visualRep[i][j] = " * ";
        } else {
            visualRep[i][j] = "   ";
        }
    }

    private static void addPriceToLastRowInGraph(String[][] visualRep, int i, int j) {
        if (i == 5) {
            if (electricPrices[j - 2].getPrice() >= lowestPrice) {
                visualRep[i][j] = " * ";
            } else {
                visualRep[i][j] = "   ";
            }
        }
    }

    private static void printVisualGraph(String[][] visualRep) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 26; j++) {
                System.out.print(visualRep[i][j]);
            }
            System.out.println("");
        }
    }
}
