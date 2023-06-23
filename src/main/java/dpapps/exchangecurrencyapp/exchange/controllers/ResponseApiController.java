package dpapps.exchangecurrencyapp.exchange.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dpapps.exchangecurrencyapp.configuration.AppVariables;
import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.LocationCurrencyPairRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.LocationRepository;
import dpapps.exchangecurrencyapp.exchange.tools.AvailableCurrencyTypesChecker;
import dpapps.exchangecurrencyapp.exchange.tools.ConversionLocalDateString;
import dpapps.exchangecurrencyapp.exchange.tools.DateRange;
import dpapps.exchangecurrencyapp.jsonparser.responseexchanges.AllCurrencyExchangesFromSingleDayPojo;
import org.hibernate.grammars.hql.HqlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ResponseApiController {
    CurrencyRepository currencyRepository;

    ExchangeRepository exchangeRepository;

    LocationRepository locationRepository;

    LocationCurrencyPairRepository locationCurrencyPairRepository;

    @Autowired
    public ResponseApiController(CurrencyRepository currencyRepository,
                                 ExchangeRepository exchangeRepository,
                                 LocationRepository locationRepository,
                                 LocationCurrencyPairRepository locationCurrencyPairRepository) {
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
    public String getLatestForCurrency(@RequestParam String currency,
                                       @RequestParam String startDate,
                                       @RequestParam String finishDate) {

        /**
         * Convert String Date to LocalDate
         */
        LocalDate beginDate = ConversionLocalDateString.convertStringToLocalDate(startDate);
        LocalDate endDate = ConversionLocalDateString.convertStringToLocalDate(finishDate);
        LocalDate onesDate = LocalDate.of(1,1,1);

        /**
         * Check if parameters are empty
         */

        if (beginDate.isEqual(onesDate) || endDate.isEqual(onesDate)) {
            beginDate = endDate = LocalDate.now();
        }
        if (beginDate.isEqual(onesDate)) {
            beginDate=endDate;
        } else if (endDate.isEqual(onesDate)) {
            endDate=beginDate;
        }


        /**
         * Check if dates are not in valid range, if they are not, return correct date range.
         */

        if (DateRange.isDateInValidRange(beginDate, endDate) == false) {
            LocalDate[] beginEndDate = DateRange.returnValidRange(beginDate, endDate);
            beginDate = beginEndDate[0];
            endDate = beginEndDate[1];
        }


        Optional<AllCurrencyExchangesFromSingleDayPojo> pojo = Optional.empty();
        String returnedJson = "";
        List<AllCurrencyExchangesFromSingleDayPojo> pojoList = new ArrayList<>();

        /**
         * Check if requested exchange rates are form a single day, if true - return exchanges form the day.
         * If the exchange rates are from multiple days, return list of exchanges.
         */

        if (beginDate.isEqual(endDate)) {
            pojo = getExchangesFromSingleDay(beginDate, currency);
            if (pojo.isEmpty()) {
                return "Failed to return exchange rates";
            }

            returnedJson = buildJsonFromPojo(pojo.get());

            if (returnedJson.equals("")) {
                return "Json body is empty";
            }
        }
        else {
            pojoList = getExchangesFromMultipleDays(beginDate, endDate, currency);
            returnedJson = buildJsonFromPojo(pojoList);
        }

        return returnedJson;
    }

    public Optional<AllCurrencyExchangesFromSingleDayPojo> getExchangesFromSingleDay(LocalDate inputDate,
                                                                                     String currency) {


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

        if (exchangeRepository.existsByExchangeDate(exchangeDate) == false) {
            pojo.setSuccess(false);
            return Optional.ofNullable(pojo);
        }

        /**
         * Fetch exchanges from database
         */
        try {
            List<Exchange> latestExchangesList;

            /**
             * Currency checks
             */
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

            /**
             * Get exchange rates from db
             */
            Map<String, Double> rates = new HashMap<>();
            for (Exchange entry: latestExchangesList
            ) {
                rates.put(entry.getCurrency().getIsoName(), entry.getValue());
            }
            pojo.setDate(exchangeDate);
            pojo.setRates(rates);
            pojo.setSuccess(true);
        }
        catch (Exception e) {
            System.out.println("Could not acquire latest exchange data: Exception: "+ e);
        }

        return Optional.ofNullable(pojo);
    }

    public List<AllCurrencyExchangesFromSingleDayPojo> getExchangesFromMultipleDays(LocalDate beginDate,
                                                                                              LocalDate endDate,
                                                                                              String currency) {

        List<AllCurrencyExchangesFromSingleDayPojo> exchangeList = new ArrayList<>();

        LocalDate currentDate = beginDate;
        while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
            AllCurrencyExchangesFromSingleDayPojo singleDayExchanges = getExchangesFromSingleDay(currentDate, currency).get();
            if (singleDayExchanges.isSuccess()) {
                exchangeList.add(singleDayExchanges);
            }
            currentDate = currentDate.plusDays(1);
        }

        return exchangeList;

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

    public String buildJsonFromPojo(List<AllCurrencyExchangesFromSingleDayPojo> pojoList) {

        ObjectMapper objectMapper = new ObjectMapper();
        String exchangesToJson = "";
        try {
            exchangesToJson = objectMapper.writeValueAsString(pojoList);
        }
        catch (Exception e) {
            System.out.println("Could not map object to JSON. Exception: "+ e);
        }

        return exchangesToJson;
    }

    public LocalDate returnExchangeDateThatExistsInDb(LocalDate date) {

        while (DateRange.isDateInValidRange(date)) {
            if (exchangeRepository.existsByExchangeDate(date) == false) {
                date = date.minusDays(1);
            }
            else {
                return date;
            }
        }
        return AppVariables.EXCHANGE_DATE_OLDEST;
    }

}
