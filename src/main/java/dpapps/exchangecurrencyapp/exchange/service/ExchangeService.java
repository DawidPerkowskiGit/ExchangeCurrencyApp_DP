package dpapps.exchangecurrencyapp.exchange.service;

import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonConvertable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ExchangeService {


    /**
     * Fetches available currencies from the database
     *
     * @param date Optional parameter - currencies active at specified date
     */
    ResponseEntity<JsonConvertable> getCurrencies(String date);

    /**
     * Fetches currencies with countries and other locations where they can be used in.
     */
    ResponseEntity<List<JsonConvertable>> getCurrenciesAndLocations();

    /**
     * Admin only accessible endpoint which performs requested URL call. It can be used to perform currency exchanges import.
     */
    String manualRequestUrl(String apiKey, String requestUrl);


    /**
     * Fetches exchange rates from the database
     *
     * @param apiKey        Required attribute
     * @param currency      Optional attribute - convert to
     * @param baseCurrency  Optional attribute - convert from
     * @param startDate     Optional attribute
     * @param finishDate    Optional attribute
     * @param currencyValue Optional attribute
     * @param headers       List of http headers
     */
    ResponseEntity<JsonConvertable> getExchanges(String apiKey, String currency, String baseCurrency, String startDate, String finishDate, Map<String, String> headers, String currencyValue);

}
