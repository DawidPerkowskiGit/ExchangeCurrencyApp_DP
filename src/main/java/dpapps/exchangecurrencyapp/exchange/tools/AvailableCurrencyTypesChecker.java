package dpapps.exchangecurrencyapp.exchange.tools;

/**
 * Tool that checks if currency is available
 */
public class AvailableCurrencyTypesChecker {

    /**
     * Method which confirms or denies if currency is available
     *
     * @param currency Requested currency
     * @return Boolean result
     */
    public static boolean isThisCurrencyAvailable(String currency) {
        for (AvailableCurrencyTypes entry : AvailableCurrencyTypes.values()) {
            if (currency.equals(entry.toString())) {
                return true;
            }
        }
        return false;
    }
}
