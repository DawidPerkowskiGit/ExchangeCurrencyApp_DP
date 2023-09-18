package dpapps.exchangecurrencyapp.exchange.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class CurrencyListExtractor {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyListExtractor.class);
    public static List<String> extractCurrencyList(String input) {
        String[] splitResult = new String[0];
        List<String> currenciesList = new LinkedList<>();
        try {
            splitResult = input.split(",");
        }
        catch (Exception e) {
            logger.error("Could not extract currencies from input. Exception: "+ e);
            currenciesList.add("");
            return currenciesList;
        }

        for (String singleWord: splitResult
             ) {
            if (AvailableCurrencyTypesChecker.isThisCurrencyAvailable(singleWord)) {
                currenciesList.add(singleWord);
            }
        }
        return currenciesList;
    }
}
