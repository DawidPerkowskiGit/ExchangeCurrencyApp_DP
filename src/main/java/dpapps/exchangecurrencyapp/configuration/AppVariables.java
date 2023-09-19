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
    public final static String DEFAULT_BASE_CURRENCY = "EUR";

    /**
     * Default requested currency
     */
    public final static String DEFAULT_REQUESTED_CURRENCY = "USD";


    public final static String ALL_CURRENCIES = "ALL_CURRENCIES";

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

    /**
     * Server date format
     */

    public final static LocalDate invalidDateCheck = LocalDate.of(1, 1 ,1);

    public final static String DATE_FORMAT = "yyyy-MM-dd";

}
