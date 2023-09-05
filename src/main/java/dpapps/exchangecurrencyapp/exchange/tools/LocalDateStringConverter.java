package dpapps.exchangecurrencyapp.exchange.tools;

import java.time.LocalDate;

/**
 * Tool which converts Date in yyyy-MM-dd format to String or vice-versa
 */
public class LocalDateStringConverter {

    /**
     * Convert String to Date format
     *
     * @param string Date in string format
     * @return Date in LocalDate format
     */
    public static LocalDate convertStringToLocalDate(String string) {
        int[] intDate = new int[3];
        intDate = putOnesIntoArray(intDate);
        try {
            intDate = convertStringDateToInts(string);
        } catch (Exception e) {
            System.out.println("Cant convert String do LocalDate. Exception: " + e);
        }
        return LocalDate.of(intDate[0], intDate[1], intDate[2]);
    }

    /**
     * Convert String to three ints format
     *
     * @param date Date in String format
     * @return Date converted to Array of three ints
     */
    public static int[] convertStringDateToInts(String date) {
        String[] strings = date.split("-");
        int arr[] = new int[3];
        arr = putOnesIntoArray(arr);
        if (strings.length != 3) {
            System.out.println("Cant convert String date to int date. It was not split into three parts");
            return arr;
        }
        for (int iterator = 0; iterator < 3; iterator++) {
            try {
                arr[iterator] = Integer.valueOf(strings[iterator]);
            } catch (Exception e) {
                System.out.println("Cannot convert String date to int, some of the values cant be converted");
                arr = putOnesIntoArray(arr);
            }
        }
        return arr;
    }

    /**
     * Fill array with ones
     *
     * @param array Input array
     * @return Array with values of 1
     */

    public static int[] putOnesIntoArray(int[] array) {
        for (int arrayIterator = 0; arrayIterator < array.length; arrayIterator++) {
            array[arrayIterator] = 1;
        }
        return array;
    }

}
