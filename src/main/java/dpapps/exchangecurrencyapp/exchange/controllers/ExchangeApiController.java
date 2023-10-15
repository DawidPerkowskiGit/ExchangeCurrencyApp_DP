package dpapps.exchangecurrencyapp.exchange.controllers;

import dpapps.exchangecurrencyapp.exchange.service.ExchangeService;
import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonConvertable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controller defining all REST API endpoints
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ExchangeApiController {

    private final ExchangeService exchangeService;

    @Autowired
    public ExchangeApiController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    /**
     * Fetched available currencies from the database
     *
     * @param date Optional parameter - currencies active at specified date
     * @return Currencies List in JSON format
     */

    @GetMapping("/currencies")
    public ResponseEntity<JsonConvertable> getCurrencies(@RequestParam(required = false) String date) {
        return exchangeService.getCurrencies(date);

    }

    /**
     * Fetched currencies and countries/other locations where they can be used in.
     *
     * @return Currencies list with all the countries they can be used in returned in JSON format
     */

    @GetMapping("/currencies/locations")
    public ResponseEntity<List<JsonConvertable>> getCurrenciesAndLocations() {
        return exchangeService.getCurrenciesAndLocations();
    }

    /**
     * Admin only accessible endpoint which performs requested URL requested. It was used to perform currency exchanges import.
     *
     * @param apiKey     REST APi key
     * @param requestUrl Requested URL
     * @return status result of currency import requestapi from the scheduler
     */
    @GetMapping("/manualRequestUrl")
    public String forceRequestUrl(@RequestParam(required = false) String apiKey, @RequestParam(required = false) String requestUrl) {
        return exchangeService.manualRequestUrl(apiKey, requestUrl);
    }

    /**
     * Fetches exchange rates from the database
     *
     * @param currency     Optional http attribute - Requested currency
     * @param startDate    Optional http attribute - start date for exchange rated from multiple days
     * @param finishDate   Optional http attribute - finish date for exchange rated from multiple days
     * @param baseCurrency Optional http attribute - http attribute Base currency
     * @param apiKey       Required http attribute - User's API key
     * @param headers      List of http headers
     * @return Exchange rates in JSON format
     */

    @GetMapping("/exchange")
    public ResponseEntity<JsonConvertable> getExchangeRates(@RequestParam(required = false) String currency, @RequestParam(required = false) String startDate, @RequestParam(required = false) String finishDate, @RequestParam(required = false) String baseCurrency, @RequestParam(required = false) String apiKey, @RequestHeader Map<String, String> headers, @RequestParam(required = false) String currencyValue) {
        return exchangeService.getExchanges(apiKey, currency, baseCurrency, startDate, finishDate, headers, currencyValue);
    }
}
