package dpapps.exchangecurrencyapp.exchange.helpers;

/**
 * Tool that checks if currency is available
 */
public class CurrencyTypesValidator {

    /**
     * Method which confirms or denies if currency is available
     *
     * @param currency Requested currency
     * @return Boolean result
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
