package dpapps.exchangecurrencyapp.exchange.controllers;

import dpapps.exchangecurrencyapp.exchange.service.ExchangeService;
import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonConvertable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
     * Gets available currencies from the database
     *
     * @param date Optional parameter - currencies usable at specified date (some countries migrated to other currencies, previous one is not usable anymore)
     * @return Currencies List in JSON format
     */

    @GetMapping("/currencies")
    public ResponseEntity<JsonConvertable> getCurrencies(@RequestParam(required = false) String date) {
        return exchangeService.getCurrencies(date);

    }

    /**
     * Gets currencies and countries/other locations where they can be used in.
     *
     * @return Currencies list with all the countries they can be used in returned in JSON format
     */

    @GetMapping("/currencies/locations")
    public ResponseEntity<List<JsonConvertable>> getCurrenciesAndLocations() {
        return exchangeService.getCurrenciesAndLocations();
    }


    /**
     * Gets exchange rates from the database
     *
     * @param apiKey        Required attribute - User's API key
     * @param currency      Optional attribute - Convert TO currency, if none specified all currencies will be returned
     * @param baseCurrency  Optional attribute - Convert FROM currency, if none specified EUR will be used as default
     * @param startDate     Optional attribute - Start date of exchanges
     * @param finishDate    Optional attribute - Finish date of exchanges.
     *                      If neither of the dates was provided, the latest exchanges will be returned.
     *                      If only one date was provided, exchanges will be fetched from the single (provided) date
     * @param currencyValue Optional attribute - Used to specify the currency conversion value
     * @param headers       List of http headers
     * @return Exchange rates in JSON format
     */

    @GetMapping("/exchange")
    public ResponseEntity<JsonConvertable> getExchangeRates(@RequestParam(required = false) String currency, @RequestParam(required = false) String startDate, @RequestParam(required = false) String finishDate, @RequestParam(required = false) String baseCurrency, @RequestParam(required = false) String apiKey, @RequestHeader Map<String, String> headers, @RequestParam(required = false) String currencyValue) {
        return exchangeService.getExchanges(apiKey, currency, baseCurrency, startDate, finishDate, headers, currencyValue);
    }
}
