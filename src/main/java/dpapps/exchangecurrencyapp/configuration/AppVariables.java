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

    /**
     * Value set/used when exchanges of all currencies have to be fetched
     */
    public final static String ALL_CURRENCIES = "ALL_CURRENCIES";

    /**
     * Daily limit of REST API uses for registered users
     */
    public final static int DAILY_USE_LIMIT = 5;


    /**
     * Authorized frontend application URL
     */

    public final static String FRONTEND_APP_URL = "https://dp-exchange-currency-app-ng.onrender.com";

    /**
     * Server date format
     */

    public final static String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Invalid date checker value
     */
    public final static LocalDate INVALID_DATE_VALUES = LocalDate.of(1, 1 ,1);

    /**
     * Status code for API key checker when use limit was reached
     */
    public final static int API_KEY_USE_LIMIT_REACHED = 0;

    /**
     * Status code for API key checker when API key is inactive
     */
    public final static int API_KEY_INACTIVE = 1;

    /**
     * Status code for API key checker when API key is invalid
     */
    public final static int API_KEY_INVALID = 2;

    /**
     * Status code for API key checker when API key belongs to an admin
     */
    public final static int API_KEY_ADMIN = 3;

    /**
     * Status code for API key checker when user is not locked
     */
    public final static int API_KEY_USER_NOT_LOCKED = 4;

    /**
     * Status code for API key checker when API key is valid
     */
    public final static int API_KEY_VALID = 5;

    /**
     * Status code for API key checker when API was not provided
     */
    public final static int API_KEY_NOT_PROVIDED = 6;


    public final static int RETURNED_JSON_BODY_FORBIDDEN = 403;
    /**
     * Valid API key message
     */
    public final static String MESSAGE_VALID_API_KEY = "Api key is valid";


    /**
     * Error message used in returned JSON body when API key was not provided
     */
    public final static String ERROR_BODY_API_KEY_NOT_PROVIDED = "You did not provide an API KEY";

    /**
     * Error message used in returned JSON body when API key was not valid
     */
    public final static String ERROR_BODY_API_KEY_INVALID = "Provided API KEY is invalid";

    /**
     * Error message used in returned JSON body when API key uses limit was reached
     */
    public final static String ERROR_BODY_API_KEY_USE_LIMIT_REACHED = "You reached API request limit";

    /**
     * Error message used in returned JSON body when API key used is inactive
     */
    public final static String ERROR_BODY_API_KEY_INACTIVE = "Provided API KEY is inactive";

    /**
     * Logged message when exchanges endpoint was called
     */
    public final static String LOGGER_EXCHANGE_ENDPOINT_CALLED = "/api/exchange called";

    /**
     * Logged message when currencies endpoint was called
     */
    public final static String LOGGER_CURRENCIES_ENDPOINT_CALLED = "api/currencies called";

    /**
     * Logged message when currencies/locations endpoint was called
     */
    public final static String LOGGER_CURRENCIES_LOCATIONS_ENDPOINT_CALLED = "api/currencies/locations called";

    public final static String LOGGER_MANUAL_REQUEST_URL_EMPTY = "Request API URL is null";

    /**
     * Logged message when API was called by the frontend application
     */
    public final static String LOGGER_FRONTEND_APP_REQUEST = "API request by Frontend app client";

    /**
     * Origin header name
     */
    public final static String HEADER_ORIGIN = "origin";

    /**
     * Role admin name
     */
    public final static String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     * Error message displayed when locked user tries to generate an API key
     */
    public final static String USER_IS_LOCKED = "Could not generate new API key, account is locked";





}
