package dpapps.exchangecurrencyapp.configuration;

import java.time.LocalDate;

/**
 * Class which stores all global variables
 */
public class AppVariables {
    /**
     * Oldest exchange rates entry in the database
     */
    public final static LocalDate EXCHANGE_DATE_OLDEST = LocalDate.of(1999, 1, 4);

    /**
     * Default currency used as base for returning exchange rates
     */
    public final static String CURRENCY_BASE = "EUR";

    /**
     * Daily limit of REST API uses for registered users
     */
    public final static int DAILY_LIMIT_OF_DAILY_USAGES = 5;

    /**
     * Valid API key message
     */
    public final static String VALID_API_KEY_MESSAGE = "Api key is valid";

    /**
     * Authorized frontend application URL
     */

    public final static String FRONTEND_APP_URL = "https://dp-exchange-currency-app-ng.onrender.com";
}
