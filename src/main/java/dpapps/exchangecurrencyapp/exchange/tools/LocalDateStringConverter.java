package dpapps.exchangecurrencyapp.exchange.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Tool which converts Date in yyyy-MM-dd format to String or vice-versa
 */
public class LocalDateStringConverter{

    private DateTimeFormatter dateFormat;

    private static final Logger logger = LoggerFactory.getLogger(LocalDateStringConverter.class);


    /**
     * Convert String to Date format
     *
     * @param string Date in string format
     * @return Date in LocalDate format
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



//        int[] intDate = new int[3];
//        intDate = putOnesIntoArray(intDate);
//        try {
//            intDate = convertStringDateToInts(string);
//        } catch (Exception e) {
//            System.out.println("Cant convert String do LocalDate. Exception: " + e);
//        }
//        return LocalDate.of(intDate[0], intDate[1], intDate[2]);
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
