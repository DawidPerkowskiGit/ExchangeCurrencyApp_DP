package dpapps.exchangecurrencyapp.exchange.helpers;

import dpapps.exchangecurrencyapp.configuration.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Tool which converts Date in yyyy-MM-dd format to String or vice-versa
 */
public class DateStringConverter {

    private DateTimeFormatter dateFormat;

    private static final Logger logger = LoggerFactory.getLogger(DateStringConverter.class);


    /**
     * Convert String to Date format
     */
    public static LocalDate convertStringToLocalDate(String string) {
        LocalDate date;
        try {
            date = LocalDate.parse(string);
        }
        catch ( Exception e) {
            return LocalDate.of(1, 1, 1);
        }
        return date;
    }

    /**
     * Convert String  to three ints.
     *
     * @param date date separated by minuses, for example 2020-10-10
     */
    public static int[] convertDateStringToInts(String date) {
        String[] strings = date.split(AppConstants.DATE_STRING_SEPARATOR);
        int arr[] = new int[3];
        arr = putOnesIntoArray(arr);
        if (strings.length != 3) {
            logger.info("Can't convert String date to int date. It was not split into three parts");
            return arr;
        }
        for (int iterator = 0; iterator < 3; iterator++) {
            try {
                arr[iterator] = Integer.valueOf(strings[iterator]);
            } catch (Exception e) {
                logger.warn("Can't convert String date to int, some of the values cant be converted. Exception: "+ e);
                arr = putOnesIntoArray(arr);
            }
        }
        return arr;
    }

    /**
     * Fill array with ones
     */

    public static int[] putOnesIntoArray(int[] array) {
        for (int arrayIterator = 0; arrayIterator < array.length; arrayIterator++) {
            array[arrayIterator] = 1;
        }
        return array;
    }

}
