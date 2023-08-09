package dpapps.exchangecurrencyapp.configuration;

import java.time.LocalDate;

public class AppVariables {
    public final static LocalDate EXCHANGE_DATE_OLDEST = LocalDate.of(1999, 01, 04);
    public final static String CURRENCY_BASE = "EUR";

    public final static int GLOBAL_LIMIT_OF_DAILY_USAGES = 5;

    public final static String VALID_API_KEY_MESSAGE = "Api key is valid";

    public final static String FRONTEND_APP_URL = "https://dp-exchange-currency-app-ng.onrender.com";
}
