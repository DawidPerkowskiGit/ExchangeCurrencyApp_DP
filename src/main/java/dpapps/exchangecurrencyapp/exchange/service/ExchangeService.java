package dpapps.exchangecurrencyapp.exchange.service;

import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonConvertable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ExchangeService {

    /**
     * Fetched available currencies from the database
     *
     * @param date Optional parameter - currencies active at specified date
     * @return Currencies List in JSON format
     */
//    public ResponseEntity<CurrencyIsoNameToFullNameMapper> getCurrencies(String date);
    ResponseEntity<JsonConvertable> getCurrencies(String date);

    /**
     * Fetched currencies and countries/other locations where they can be used in.
     *
     * @return Currencies list with all the countries they can be used in returned in JSON format
     */
    ResponseEntity<List<JsonConvertable>> getCurrenciesAndLocations();

    /**
     * Admin only accessible endpoint which performs requested URL requested. It was used to perform currency exchanges import.
     *
     * @param apiKey     REST APi key
     * @param requestUrl Requested URL
     * @return status result of currency import requestapi from the scheduler
     */
    String manualRequestUrl(String apiKey, String requestUrl);


    /**
     * Fetches exchange rates from the database
     *
     * @param apiKey       Required http attribute - User's API key
     * @param currency     Optional http attribute - Requested currency
     * @param baseCurrency Optional http attribute - http attribute Base currency
     * @param startDate    Optional http attribute - start date for exchange rated from multiple days
     * @param finishDate   Optional http attribute - finish date for exchange rated from multiple days
     * @param headers      List of http headers
     * @return Exchange rates in JSON format
     */
    ResponseEntity<JsonConvertable> getExchanges(String apiKey, String currency, String baseCurrency, String startDate, String finishDate, Map<String, String> headers, String currencyValue);

    /**
     * Returns exchange rates from multiple days. It is called when accessing "../exchange" endpoint
     *
     * @param beginDate    Exchange rates starting from date
     * @param endDate      Exchange rates ending in date
     * @param currency     Requested currency
     * @param baseCurrency Base currency
     * @return List of exchange rates
     */
    List<JsonConvertable> getExchangesFromMultipleDays(LocalDate beginDate, LocalDate endDate, List<String> currency, String baseCurrency);

    /**
     * Exchange rates from single day. It is called at least once when returning exchange rates from multiple days
     *
     * @param inputDate    Exchange rates date
     * @param currency     Requested currency
     * @param baseCurrency Base currency
     * @return Object containing exchange rated from single day
     */
    JsonConvertable getExchangesFromSingleDay(LocalDate inputDate, List<String> currency, String baseCurrency);

    /**
     * Method which returns exchange rate based on non-default currency
     *
     * @param date     Exchange rates date
     * @param currency Currency used as base
     * @return Exchange rate based on requested currency
     */
    double calculateNewRatio(LocalDate date, String currency);

    /**
     * Converts returned database rows to object ready for serialization
     *
     * @param databaseEntries entire retrieved from the database, structure [iso_name, full_name, location]
     * @return Object of structure [iso_name, full_name, List<location>] ready for serialization
     */
    List<JsonConvertable> convertDbCurrencyNameLocationToObjectList(Iterable<String[]> databaseEntries);

    /**
     * Adds entry to List property of a HashMap
     *
     * @param value Value needed to be added to the List
     * @param list  List of entries
     * @return List with an additional value
     */
    List<String> addValueToList(String value, List<String> list);
}
