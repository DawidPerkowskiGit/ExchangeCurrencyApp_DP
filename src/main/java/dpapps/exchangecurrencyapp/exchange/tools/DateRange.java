package dpapps.exchangecurrencyapp.exchange.tools;

import dpapps.exchangecurrencyapp.configuration.DateVariables;

import java.time.LocalDate;

/**
 * This module is responsible for providing valid range of exchange dates stored in the database.
 */

public class DateRange {

    public static boolean isDateInValidRange(LocalDate date) {
        if (date.isBefore(DateVariables.EXCHNAGE_DATE_OLDEST)) {
            return false;
        }
        if (date.isAfter(LocalDate.now())) {
            return false;
        }
        return true;
    }

    public static boolean isDateInValidRange(LocalDate left, LocalDate right) {
        LocalDate temp;
        if (right.isBefore(left)) {
            temp = right;
            right = left;
            left = temp;
        }

        if (left.isBefore(DateVariables.EXCHNAGE_DATE_OLDEST)) {
            return false;
        }
        if (right.isBefore(DateVariables.EXCHNAGE_DATE_OLDEST)) {
            return false;
        }
        if (left.isAfter(LocalDate.now())) {
            return false;
        }
        if (right.isAfter(LocalDate.now())) {
            return false;
        }

        return true;
    }

    public static LocalDate returnValidRange(LocalDate date) {
        if (date.isBefore(DateVariables.EXCHNAGE_DATE_OLDEST)) {
            return DateVariables.EXCHNAGE_DATE_OLDEST;
        }
        if (date.isAfter(LocalDate.now())) {
            return LocalDate.now();
        }
        return date;
    }

    public static LocalDate[] returnValidRange(LocalDate left, LocalDate right) {
        LocalDate temp;
        if (right.isBefore(left)) {
            temp = right;
            right = left;
            left = temp;
        }

        if (left.isBefore(DateVariables.EXCHNAGE_DATE_OLDEST)) {
            left = DateVariables.EXCHNAGE_DATE_OLDEST;
        }
        if (right.isBefore(DateVariables.EXCHNAGE_DATE_OLDEST)) {
            right = DateVariables.EXCHNAGE_DATE_OLDEST;
        }
        if (left.isAfter(LocalDate.now())) {
            left = LocalDate.now();
        }
        if (right.isAfter(LocalDate.now())) {
            right = LocalDate.now();
        }

        LocalDate[] returnDates = new LocalDate[2];
        returnDates[0] = left;
        returnDates[1] = right;
        return returnDates;
    }

}
