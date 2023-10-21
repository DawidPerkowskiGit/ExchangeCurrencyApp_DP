package dpapps.exchangecurrencyapp.exchange.helpers;

import dpapps.exchangecurrencyapp.configuration.AppConstants;

import java.time.LocalDate;

/**
 * This module is responsible for providing valid range of exchange dates stored in the database.
 * Valid is between first and last entry in the database
 */

public class DateRangeValidator {

    /**
     * Checks if requested date is in valid range.
     */
    public static boolean isDateInValidRange(LocalDate date, LocalDate latestDate) {
        if (date.isBefore(AppConstants.EXCHANGE_DATE_OLDEST)) {
            return false;
        }
        if (date.isAfter(latestDate)) {
            return false;
        }
        return true;
    }

    /**
     * Checks if both dates are in valid range
     */

    public static boolean areDatesInValidRange(LocalDate left, LocalDate right, LocalDate latestDate) {
        if (right.isBefore(left)) {
            return false;
        }
        if (left.isBefore(AppConstants.EXCHANGE_DATE_OLDEST)) {
            return false;
        }
        if (right.isBefore(AppConstants.EXCHANGE_DATE_OLDEST)) {
            return false;
        }
        if (left.isAfter(latestDate)) {
            return false;
        }
        if (right.isAfter(latestDate)) {
            return false;
        }

        return true;
    }

    /**
     * Corrects the date to be in correct range
     */

    public static LocalDate returnValidRange(LocalDate date, LocalDate latestDate) {
        if (date.isBefore(AppConstants.EXCHANGE_DATE_OLDEST)) {
            return AppConstants.EXCHANGE_DATE_OLDEST;
        }
        if (date.isAfter(latestDate)) {
            return latestDate;
        }
        return date;
    }

    /**
     * Corrects both dates to be on correct range
     */

    public static LocalDate[] returnValidRange(LocalDate left, LocalDate right, LocalDate latestDate) {
        LocalDate temp;
        if (right.isBefore(left)) {
            temp = right;
            right = left;
            left = temp;
        }

        if (left.isBefore(AppConstants.EXCHANGE_DATE_OLDEST)) {
            left = AppConstants.EXCHANGE_DATE_OLDEST;
        }
        if (right.isBefore(AppConstants.EXCHANGE_DATE_OLDEST)) {
            right = AppConstants.EXCHANGE_DATE_OLDEST;
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
