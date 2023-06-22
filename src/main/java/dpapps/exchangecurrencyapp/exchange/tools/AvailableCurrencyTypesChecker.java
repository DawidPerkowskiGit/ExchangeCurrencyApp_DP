package dpapps.exchangecurrencyapp.exchange.tools;

public class AvailableCurrencyTypesChecker {
        public static boolean isThisCurrencyAvailable(String currency) {
            for (AvailableCurrencyTypes entry : AvailableCurrencyTypes.values()
            ) {
                if (currency.equals(entry.toString())) {
                    return true;
                }
            }
            return false;
        }
}
