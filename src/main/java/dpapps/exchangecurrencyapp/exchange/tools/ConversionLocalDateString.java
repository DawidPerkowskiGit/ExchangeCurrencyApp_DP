package dpapps.exchangecurrencyapp.exchange.tools;

import java.time.LocalDate;

public class ConversionLocalDateString {

    public static String convertLocalDateToString(LocalDate localDate) {
        return localDate.toString();
    }

    public static LocalDate convertStringToLocalDate(String string) {
        int[] intDate = convertStringDateToInts(string);
        return LocalDate.of(intDate[0], intDate[1], intDate[2]);
    }

    public static int[] convertStringDateToInts(String date) {
        String[] strings = date.split("-");
        int arr[] = new int[strings.length];
        for (int iterator = 0; iterator < strings.length; iterator++) {
            arr[iterator] = Integer.valueOf(strings[iterator]);
        }
        return arr;
    }

}
