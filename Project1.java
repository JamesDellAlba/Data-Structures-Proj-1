import java.util.Scanner;
import java.io.File;

public class Project1 {
    /**
     * COP 3530: Project 1–Array Searches and Sorts
     * <p>
     *     After the user inputs a file name, this class takes input from the user in the form of an integer from 1-6.
     *     Each choice is used to determine which methods to call. Output is generated when the user requests to print the
     *     contents of the array of countries, which may be sorted in different ways depending on the previous user input.
     * </p>
     *
     * @author James Dell'Alba
     * @version 1/28/2020
     */
    public static void main(String[] args) throws java.io.FileNotFoundException{
        Scanner input = new Scanner(System.in);
        System.out.println("enter the name of the file");
        String fileName = input.next();
        File countries = new File(fileName);
        Scanner fileInput = new Scanner(countries);
        fileInput.useDelimiter(",|\n"); //set the delimiter to comma or newline

        String firstLine = fileInput.nextLine(); //move the cursor past the first line of the file

        //create an array of countries
        Country[] countryArray = new Country[156];
        for(int i = 0; i < 155; i++) {
            //fill the array with Country objects
            String name = fileInput.next();
            String code = fileInput.next();
            String capitol = fileInput.next();
            long population = Double.valueOf(fileInput.next()).longValue();
            long gdp = Double.valueOf(fileInput.next()).longValue();
            long happiness = Double.valueOf(fileInput.next()).longValue();
            countryArray[i] = new Country(name, code, capitol, population, gdp, happiness);
        }
        boolean sortedByName = false; //keep track of whether the array is sorted or not (initially false)

        int exit = 0;
        while(exit == 0) {
            //main loop for interacting with the user
            System.out.println("\nEnter a number:");
            System.out.println("1) print a countries report");
            System.out.println("2) Sort by name (bubble sort)");
            System.out.println("3) Sort by happiness rank (selection sort)");
            System.out.println("4) Sort by GDP per capita (insertion sort)");
            System.out.println("5) Find and print a country");
            System.out.println("6) quit");
            int entry = input.nextInt();

            //deal with user input
            switch(entry) {
                case 1:
                    System.out.printf("%-33s %-10s %-20s %-15s %-25s %-10s\n", "Name", "Code", "Capitol", "Population", "GDP", "Happiness");
                    for(int i = 0; i < 155; i++) {
                        countryArray[i].printCountries();
                    }
                    break;
                case 2:
                    bubbleSortNames(countryArray, 155);
                    sortedByName = true;
                    System.out.println("array sorted");
                    break;
                case 3:
                    selectionSortHappiness(countryArray, 155);
                    sortedByName = false;
                    System.out.println("array sorted");
                    break;
                case 4:
                    insertionSortGDPperCapita(countryArray, 155);
                    sortedByName = false;
                    System.out.println("array sorted");
                    break;
                case 5:
                    System.out.println("Enter the name of a country:");
                    String target = input.next();
                    findCountry(countryArray, 155, sortedByName, target);
                    break;
                case 6:
                    exit++;
                    break;
                default:
                    System.out.println("please enter 1-6");
            }
        }

        fileInput.close();
        input.close();
    }

    static void bubbleSortNames(Country[] list, int num) {
        /**
         * sort the array by country name using bubblesort
         * input is the array of countries and the number of countries in the array
         * returns void after sorting
         *
         * @param list the array of country objects
         * @param num the size of the array
         */
        for (int outer = 0; outer < (num - 1); outer++) {
            for (int inner = num - 1; inner > outer; inner--) {
                if (list[inner].compareStrings(list[inner].getName(), list[inner - 1].getName()) == 1) {
                    Country temp = list[inner];
                    list[inner] = list[inner - 1];
                    list[inner - 1] = temp;
                }
            }
        }
    }

    static void selectionSortHappiness(Country[] list, int num) {
        /**
         * sort the array by happiness rank using selection sort
         * input is the array of countries and the size of the array, returns void after sorting
         *
         * @param list the array of country objects
         * @param num the size of the array
         */
        for (int outer = 0; outer < num - 1; outer++) {
            int lowest = outer;
            for (int inner = outer + 1; inner < num; inner++) {
                if (list[inner].getHappinessRank() < list[lowest].getHappinessRank()) {
                    lowest = inner;
                }
            }
            if (lowest != outer) {
                Country temp = list[lowest];
                list[lowest] = list[outer];
                list[outer] = temp;
            }
        }
    }

    static void insertionSortGDPperCapita(Country[] list, int num) {
        /**
         * Sort the array by GDP / Population using insertion sort
         * Input is the array of countries, returns void after sorting
         *
         * @param list the array of country objects
         * @param num the size of the array
         */
        int inner;
        int outer;
        for (outer = 1; outer < num; outer++) {
            Country temp = (list[outer]);
            inner = outer - 1;
            while (inner >= 0 && ((list[inner].getGrossDomesticProduct() / list[inner].getPopulation()) > (temp.getGrossDomesticProduct() / temp.getPopulation()))) {
                list[inner + 1] = list[inner];
                inner--;
            }
            list[inner + 1] = temp;
        }
    }

    static void findCountry(Country[] list, int num, boolean isSorted, String target) {
        /**
         * Input is the array of countries, the length of the array, whether the array is sorted by name or not,
         * and the target country.
         * This method uses binary search to find the target, but only if the array is already sorted by name.
         * Otherwise, it uses sequential search to find the target.
         *
         * This method does not call the print function of the Country class because the requested formatting is different
         *
         * @param list the array of country objects
         * @param num the size of the array
         * @param isSorted describes the state of the array (sorted by name or not)
         * @param target the country the user wishes to find
         */
        int lowerBound = 0;
        int upperBound = num - 1;
        int mid;

        if(isSorted) {
            System.out.printf("Binary Search\n");
            while (lowerBound <= upperBound) {
                mid = (lowerBound + upperBound) / 2;
                if (list[mid].compareStrings(list[mid].getName(), target) == 0) {
                    System.out.printf("%-15s %-15s\n", "Name:", list[mid].getName());
                    System.out.printf("%-15s %-15s\n", "Code:", list[mid].getCode());
                    System.out.printf("%-15s %-15s\n", "Capital:", list[mid].getCapitol());
                    System.out.printf("%-15s %-15s\n", "Population:", list[mid].getPopulation());
                    System.out.printf("%-15s %-15s\n", "GDP:", list[mid].getGrossDomesticProduct());
                    System.out.printf("%-15s %-15s\n", "Happiness:", list[mid].getHappinessRank());
                    break;
                } else if (list[mid].compareStrings(list[mid].getName(), target) == -1) {
                    upperBound = mid - 1;
                } else {
                    lowerBound = mid + 1;
                }
            }
        } else {
            System.out.printf("Sequential Search\n");
            for(int i = 1; i < num; i++) {
                if(list[i].compareStrings(list[i].getName(), target) == 0) {
                    System.out.printf("%-15s %-15s\n", "Name:", list[i].getName());
                    System.out.printf("%-15s %-15s\n", "Code:", list[i].getCode());
                    System.out.printf("%-15s %-15s\n", "Capital:", list[i].getCapitol());
                    System.out.printf("%-15s %-15s\n", "Population:", list[i].getPopulation());
                    System.out.printf("%-15s %-15s\n", "GDP:", list[i].getGrossDomesticProduct());
                    System.out.printf("%-15s %-15s\n", "Happiness:", list[i].getHappinessRank());
                }
            }
        }
    }

}
class Country {
    /**
     * This class holds data about each country in the array of countries. Name of the country,
     * country code, capital of the country, population of the country, GDP of the country, and happiness rank of the country.
     *
     * @author James Dell'Alba
     * @version 1/28/2020
     */
    private String name;
    private String code;
    private String capitol;
    private long population;
    private long grossDomesticProduct;
    private long happinessRank;

    public Country(String name, String code, String capitol, long population, long grossDomesticProduct, long happinessRank) {
        /**
         * Constructor for the country class
         * Inputs are country name, country code, country capitol, country population, country GDP, and happiness rank
         * Constructs a country object with each of the above inputs as data fields
         * No default constructor is included because it is unnecessary
         */
        this.name = name;
        this.code = code;
        this.capitol = capitol;
        this.population = population;
        this.grossDomesticProduct = grossDomesticProduct;
        this.happinessRank = happinessRank;
    }

    public String getName() {
        /**
         * getter for country name
         * no input, returns the name
         */
        return this.name;
    }

    public String getCode() {
        /**
         * getter for country code
         * no input, returns the code
         */
        return this.code;
    }

    public String getCapitol() {
        /**
         * getter for country capitol
         * no input, returns the capitol
         */
        return this.capitol;
    }

    public long getPopulation() {
        /**
         * getter for country population
         * no input, returns the population
         */
        return this.population;
    }

    public long getGrossDomesticProduct() {
        /**
         * getter for GDP
         * no input, returns the GDP
         */
        return this.grossDomesticProduct;
    }

    public long getHappinessRank() {
        /**
         * getter for happiness rank
         * no input, returns the happiness rank
         */
        return this.happinessRank;
    }

    public void setName(String newName) {
        /**
         * setter for name
         * input is the new name, returns void
         */
        this.name = newName;
    }

    public void setCode(String newCode) {
        /**
         * setter for code
         * input is the new code, returns void
         */
        this.code = newCode;
    }

    public void setCapitol(String newCapitol) {
        /**
         * setter for capitol
         * input is the new capitol, returns void
         */
        this.capitol = newCapitol;
    }

    public void setPopulation(long newPop) {
        /**
         * setter for population
         * input is the new population, returns void
         */
        this.population = newPop;
    }

    public void setGrossDomesticProduct(long newGDP) {
        /**
         * setter for GDP
         * input is the new GDP, returns void
         */
        this.grossDomesticProduct = newGDP;
    }

    public void setHappinessRank(long newHappiness) {
        /**
         * setter for happiness rank
         * input is the new rank, returns void
         */
        this.happinessRank = newHappiness;
    }

    public int compareStrings(String string1, String string2) {
        /**
         * check the alphabetical order of two strings
         * string1 comes before string2: return 1
         * string2 comes before string1: return -1
         * if they are the same string (i continues all the way to the end): return 0
         *
         * @param string1 the first string to compare
         * @param string2 the second string to compare
         */
        for (int i = 0; (i < string1.length() && i < string2.length()); i++) {
            if (string1.charAt(i) < string2.charAt(i)) {
                return 1;
            }
            else if (string1.charAt(i) > string2.charAt(i)) {
                return -1;
            }
        }
        return 0;
    }

    public void printCountries() {
        /**
         * print the contents of a country
         * no input, returns void after printing
         */
        System.out.printf("%-33s %-10s %-20s %-15d %-25d %-10d\n", this.getName(), this.getCode(), this.getCapitol(),
                this.getPopulation(), this.getGrossDomesticProduct(), this.getHappinessRank());
    }
}
