package dpapps.exchangecurrencyapp.exchange.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dpapps.exchangecurrencyapp.configuration.AppVariables;
import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.LocationCurrencyPairRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.LocationRepository;
import dpapps.exchangecurrencyapp.exchange.tools.AvailableCurrencyTypesChecker;
import dpapps.exchangecurrencyapp.exchange.tools.DateRange;
import dpapps.exchangecurrencyapp.jsonparser.responseexchanges.AllCurrencyExchangesFromSingleDayPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ResponseApiController {
    CurrencyRepository currencyRepository;

    ExchangeRepository exchangeRepository;

    LocationRepository locationRepository;

    LocationCurrencyPairRepository locationCurrencyPairRepository;

    @Autowired
    public ResponseApiController(CurrencyRepository currencyRepository, ExchangeRepository exchangeRepository, LocationRepository locationRepository, LocationCurrencyPairRepository locationCurrencyPairRepository) {
        this.currencyRepository = currencyRepository;
        this.exchangeRepository = exchangeRepository;
        this.locationRepository = locationRepository;
        this.locationCurrencyPairRepository = locationCurrencyPairRepository;
    }

/*    @GetMapping("/latest")
    public String getLatestExchange() {

        Optional<AllCurrencyExchangesFromSingleDayPojo> pojo = getExchangesFromSingleDay(LocalDate.now(), "");

        if (pojo.isEmpty()) {
            return "Failed to return exchange rates";
        }

        String returnedJson = buildJsonFromPojo(pojo.get());

        if (returnedJson.equals("")) {
            return "Json body is empty";
        }

        return returnedJson;
    }*/

    @GetMapping("/latest")
    public String getLatestForCurrency(@RequestParam String currency) {

        if (currency.isBlank()) {
            currency = "";
        }

        Optional<AllCurrencyExchangesFromSingleDayPojo> pojo = getExchangesFromSingleDay(LocalDate.now(), currency);

        if (pojo.isEmpty()) {
            return "Failed to return exchange rates";
        }

        String returnedJson = buildJsonFromPojo(pojo.get());

        if (returnedJson.equals("")) {
            return "Json body is empty";
        }

        return returnedJson;
    }

    public Optional<AllCurrencyExchangesFromSingleDayPojo> getExchangesFromSingleDay(LocalDate inputDate, String currency) {


        AllCurrencyExchangesFromSingleDayPojo pojo = new AllCurrencyExchangesFromSingleDayPojo();
        pojo.setBase(AppVariables.CURRENCY_BASE);

        /**
         * Base currency checks
         */
        if (currency.equals(AppVariables.CURRENCY_BASE)) {
            pojo.setSuccess(false);
            return Optional.ofNullable(pojo);
        }

        LocalDate exchangeDate = inputDate;

        /**
         * Exchange date checks
         */
        try {
            if (DateRange.isDateInValidRange(inputDate)==false) {
                exchangeDate = DateRange.returnValidRange(inputDate);
            }
        }
        catch (Exception e) {
            System.out.println("Could not find valid exchange rate date in the database. Exception: "+e);
        }
        pojo.setDate(exchangeDate);

        /**
         * Fetch exchanges from database
         */
        try {
            List<Exchange> latestExchangesList;

            if (currency.equals("")) {
                latestExchangesList = exchangeRepository.findAllByExchangeDateOrderByCurrencyDesc(exchangeDate);
            }
            else if (AvailableCurrencyTypesChecker.isThisCurrencyAvailable(currency)){
                latestExchangesList = exchangeRepository.findAllByExchangeDateAndCurrencyOrderByExchangeDate(exchangeDate, currency);
            }
            else {
                pojo.setSuccess(false);
                return Optional.ofNullable(pojo);
            }

            Map<String, Double> rates = new HashMap<>();
            for (Exchange entry: latestExchangesList
            ) {
                rates.put(entry.getCurrency().getIsoName(), entry.getValue());
            }
            pojo.setRates(rates);
            pojo.setSuccess(true);
        }
        catch (Exception e) {
            System.out.println("Could not acquire latest exchange data: Exception: "+ e);
        }

        return Optional.ofNullable(pojo);
    }

    public String buildJsonFromPojo(AllCurrencyExchangesFromSingleDayPojo pojo) {

        ObjectMapper objectMapper = new ObjectMapper();
        String exchangesToJson = "";
        try {
            exchangesToJson = objectMapper.writeValueAsString(pojo);
        }
        catch (Exception e) {
            System.out.println("Could not map object to JSON. Exception: "+ e);
        }

        return exchangesToJson;
    }
}
