package dpapps.exchangecurrencyapp.exchange.tools;

import dpapps.exchangecurrencyapp.configuration.AppVariables;

import java.time.LocalDate;

/**
 * This module is responsible for providing valid range of exchange dates stored in the database.
 * Valid is between first and last entry in the database
 */

public class DateRange {

    /**
     * Method checks if requested date is in valid range.
     *
     * @param date Requested date
     * @return Boolean result
     */
    public static boolean isDateInValidRange(LocalDate date, LocalDate latestDate) {
        if (date.isBefore(AppVariables.EXCHANGE_DATE_OLDEST)) {
            return false;
        }
        if (date.isAfter(latestDate)) {
            return false;
        }
        return true;
    }

    /**
     * Method checks if both dates are in valid range
     *
     * @param left  Oldest date
     * @param right Newest date
     * @return Boolean result
     */

    public static boolean isDateInValidRange(LocalDate left, LocalDate right, LocalDate latestDate) {
        if (right.isBefore(left)) {
            return false;
        }
        if (left.isBefore(AppVariables.EXCHANGE_DATE_OLDEST)) {
            return false;
        }
        if (right.isBefore(AppVariables.EXCHANGE_DATE_OLDEST)) {
            return false;
        }
//        if (left.isAfter(LocalDate.now())) {
//            return false;
//        }
//        if (right.isAfter(LocalDate.now())) {
//            return false;
//        }
        if (left.isAfter(latestDate)) {
            return false;
        }
        if (right.isAfter(latestDate)) {
            return false;
        }

        return true;
    }

    /**
     * Method which puts date in valid range.
     *
     * @param date Date to validate
     * @return Date in valid range
     */

    public static LocalDate returnValidRange(LocalDate date, LocalDate latestDate) {
        if (date.isBefore(AppVariables.EXCHANGE_DATE_OLDEST)) {
            return AppVariables.EXCHANGE_DATE_OLDEST;
        }
        if (date.isAfter(latestDate)) {
            return latestDate;
        }
        return date;
    }

    /**
     * Returns date scope in valid range
     *
     * @param left  Older date
     * @param right Newer date
     * @return Scope of dates in valid range
     */

    public static LocalDate[] returnValidRange(LocalDate left, LocalDate right, LocalDate latestDate) {
        LocalDate temp;
        if (right.isBefore(left)) {
            temp = right;
            right = left;
            left = temp;
        }

        if (left.isBefore(AppVariables.EXCHANGE_DATE_OLDEST)) {
            left = AppVariables.EXCHANGE_DATE_OLDEST;
        }
        if (right.isBefore(AppVariables.EXCHANGE_DATE_OLDEST)) {
            right = AppVariables.EXCHANGE_DATE_OLDEST;
        }
        if (left.isAfter(latestDate)) {
            left = latestDate;
        }
        if (right.isAfter(latestDate)) {
            right = latestDate;
        }

        LocalDate[] returnDates = new LocalDate[2];
        returnDates[0] = left;
        returnDates[1] = right;
        return returnDates;
    }

}
