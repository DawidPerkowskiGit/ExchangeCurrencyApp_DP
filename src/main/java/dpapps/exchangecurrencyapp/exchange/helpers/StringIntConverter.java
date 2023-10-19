package dpapps.exchangecurrencyapp.exchange.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Converts String to int
 */
@Service
public class StringIntConverter {

    private static final Logger logger = LoggerFactory.getLogger(StringIntConverter.class);

    /**
     * Checks if the String input is valid int
     *
     * @param input possible int as a String
     * @return true if its valid, false otherwise
     */
    public static Boolean isStringValidDouble(String input) {
        try {
            Double.parseDouble(input);
        } catch (Exception parsingException) {
            logger.info("Cannot convert " + input + " to a Double");
            return false;
        }
        return true;
    }

    /**
     * Converts String to int
     *
     * @param input int as a String
     * @return int parsed from String
     */
    public static Double convertStringToDouble(String input) {
        return Double.parseDouble(input);
    }
}
