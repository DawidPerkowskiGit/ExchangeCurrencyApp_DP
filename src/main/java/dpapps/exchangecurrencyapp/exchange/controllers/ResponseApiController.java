package dpapps.exchangecurrencyapp.exchange.controllers;

import dpapps.exchangecurrencyapp.exchange.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controller defining all REST API endpoints
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ResponseApiController {

    private final ExchangeService exchangeService;

    @Autowired
    public ResponseApiController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    /**
     * Currencies List
     *
     * @param date Optional parameter - currencies active at specified date
     * @return Currencies List in JSON format
     */

    @GetMapping("/currencies")
    public String getCurrencies(@RequestParam(required = false) String date) {
        return exchangeService.getCurrencies(date);

    }

    /**
     * Currencies and Countries where they can be used
     *
     * @return Currencies list with all the countries they can be used in returned in JSON format
     */

    @GetMapping("/currencies/locations")
    public String getCurrenciesAndLocations() {
        return exchangeService.getCurrenciesAndLocations();
    }

    /**
     * Admin only accessible endpoint which performs requested URL requested. It was used to perform currency exchanges import.
     *
     * @param apiKey     REST APi key
     * @param requestUrl Requested URL
     * @return status result of currency import request from the scheduler
     */
    @GetMapping("/manualRequestUrl")
    public String forceRequestUrl(@RequestParam(required = false) String apiKey, @RequestParam(required = false) String requestUrl) {
        return exchangeService.manualRequestUrl(apiKey, requestUrl);
    }

    /**
     * Exchange rated endpoints
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
    public String getExchangeRates(@RequestParam(required = false) String currency, @RequestParam(required = false) String startDate, @RequestParam(required = false) String finishDate, @RequestParam(required = false) String baseCurrency, @RequestParam(required = false) String apiKey, @RequestHeader Map<String, String> headers) {
        return exchangeService.getExchanges(currency, startDate, finishDate, baseCurrency, apiKey, headers);
    }
}
