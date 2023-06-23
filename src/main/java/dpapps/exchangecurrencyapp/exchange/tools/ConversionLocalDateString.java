package dpapps.exchangecurrencyapp.exchange.tools;

import java.time.LocalDate;

public class ConversionLocalDateString {

    public static String convertLocalDateToString(LocalDate localDate) {
        return localDate.toString();
    }

    public static LocalDate convertStringToLocalDate(String string) {
        int[] intDate = new int[3];
        intDate = putOnesIntoArray(intDate);
        try {
            intDate = convertStringDateToInts(string);
        }
        catch (Exception e) {
            System.out.println("Cant convert String do LocalDate. Exception: "+e);
        }
        return LocalDate.of(intDate[0], intDate[1], intDate[2]);
    }

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
            }
            catch (Exception e) {
                System.out.println("Cannot convert String date to int, some of the values cant be converted");
                arr = putOnesIntoArray(arr);
            }
        }
        return arr;
    }

    public static int[] putOnesIntoArray(int[] array) {
        for (int arrayIterator = 0; arrayIterator < array.length; arrayIterator++) {
            array[arrayIterator] = 1;
        }
        return array;
    }

}
