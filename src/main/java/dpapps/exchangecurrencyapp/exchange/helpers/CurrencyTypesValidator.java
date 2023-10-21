package dpapps.exchangecurrencyapp.exchange.helpers;

/**
 * Tool that checks if currency is available
 */
public class CurrencyTypesValidator {

    /**
     * Checks if specified currency is available
     */
    public static boolean isThisCurrencyAvailable(String currency) {
        for (CurrencyTypes entry : CurrencyTypes.values()) {
            if (currency.equals(entry.toString())) {
                return true;
            }
        }
        return false;
    }
}
