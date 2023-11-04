package dpapps.exchangecurrencyapp.exchange.helpers;

import dpapps.exchangecurrencyapp.configuration.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Parses and separates multiple words separated by specified value, used in splitting multiple requested currencies names separated by commas
 */
public class CurrencyListParser {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyListParser.class);

    /**
     * Splits currencies from a single String separated by commas.
     * For example "USD,EUR,PLN" will be separated to {"USD", "EUR", "PLN"}
     *
     * @param input currency names
     * @return List of currency names
     */
    public static List<String> parseCurrencyList(String input) {
        String[] splitResult;
        List<String> currenciesList = new LinkedList<>();
        try {
            splitResult = input.split(AppConstants.MULTIPLE_CURRENCIES_SEPARATOR);
        } catch (Exception e) {
            logger.error("Could not extract currencies from input. Exception: " + e);
            currenciesList.add(AppConstants.EMPTY_STRING);
            return currenciesList;
        }

        for (String singleWord : splitResult) {
            if (CurrencyTypesValidator.isThisCurrencyAvailable(singleWord)) {
                currenciesList.add(singleWord);
            }
        }
        return currenciesList;
    }
}
