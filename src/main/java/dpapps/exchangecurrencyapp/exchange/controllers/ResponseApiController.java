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
import dpapps.exchangecurrencyapp.jsonparser.responseexchanges.CurrencyExchangesFromSingleDayPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/exchange")
    public String getEchangeRates(@RequestParam(required = false) String currency,
                                  @RequestParam(required = false) String startDate,
                                  @RequestParam(required = false) String finishDate,
                                  @RequestParam(required = false) String baseCurrency) {

        /**
         * Nullable fields check
         */
        if (startDate == null) {
            startDate = "";
        }
        if (finishDate == null) {
            finishDate = "";
        }
        if (currency == null) {
            currency = "";
        }
        if (baseCurrency == null) {
            baseCurrency = "";
        }

        /**
         * Convert String Date to LocalDate
         */
        LocalDate beginDate = ConversionLocalDateString.convertStringToLocalDate(startDate);
        LocalDate endDate = ConversionLocalDateString.convertStringToLocalDate(finishDate);
        LocalDate onesDate = LocalDate.of(1,1,1);

        /**
         * Check if parameters are empty
         */

        if (beginDate.isEqual(onesDate) && endDate.isEqual(onesDate)) {
            beginDate = endDate = LocalDate.now();
        }
        if (beginDate.isEqual(onesDate)) {
            beginDate=endDate;
        } else if (endDate.isEqual(onesDate)) {
            endDate=beginDate;
        }


        /**
         * Check if dates are in valid range, if they are not, return correct date range.
         */

        if (DateRange.isDateInValidRange(beginDate, endDate) == false) {
            LocalDate[] beginEndDate = DateRange.returnValidRange(beginDate, endDate);
            beginDate = beginEndDate[0];
            endDate = beginEndDate[1];
        }


        Optional<CurrencyExchangesFromSingleDayPojo> pojo = Optional.empty();
        String returnedJson = "";
        List<CurrencyExchangesFromSingleDayPojo> pojoList = new ArrayList<>();

        /**
         * Check if requested exchange rates are form a single day, if they, are return exchanges form the day.
         * If the exchange rates are from multiple days, return list of exchanges.
         */

        if (beginDate.isEqual(endDate)) {
            pojo = getExchangesFromSingleDay(beginDate, currency, baseCurrency);
            if (pojo.isEmpty()) {
                return "Failed to return exchange rates";
            }

            returnedJson = buildJsonFromPojo(pojo.get());

            if (returnedJson.equals("")) {
                return "Json body is empty";
            }
        }
        else {
            pojoList = getExchangesFromMultipleDays(beginDate, endDate, currency, baseCurrency);
            returnedJson = buildJsonFromPojo(pojoList);
        }

        return returnedJson;
    }

    public List<CurrencyExchangesFromSingleDayPojo> getExchangesFromMultipleDays(LocalDate beginDate,
                                                                                 LocalDate endDate,
                                                                                 String currency,
                                                                                 String baseCurrency) {

        List<CurrencyExchangesFromSingleDayPojo> exchangeList = new ArrayList<>();

        LocalDate currentDate = beginDate;
        while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
            CurrencyExchangesFromSingleDayPojo singleDayExchanges = getExchangesFromSingleDay(currentDate, currency, baseCurrency).get();
            if (singleDayExchanges.isSuccess()) {
                exchangeList.add(singleDayExchanges);
            }
            currentDate = currentDate.plusDays(1);
        }

        return exchangeList;

    }

    public Optional<CurrencyExchangesFromSingleDayPojo> getExchangesFromSingleDay(LocalDate inputDate,
                                                                                  String currency,
                                                                                  String baseCurrency) {


        CurrencyExchangesFromSingleDayPojo pojo = new CurrencyExchangesFromSingleDayPojo();

        LocalDate exchangeDate = inputDate;
//        /**
//         * Exchange date checks
//         */
//        try {
//            if (DateRange.isDateInValidRange(inputDate)==false) {
//                exchangeDate = DateRange.returnValidRange(inputDate);
//            }
//        }
//        catch (Exception e) {
//            System.out.println("Could not find valid exchange rate date in the database. Exception: "+e);
//        }

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
             * Wanted currency checks
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
             * Base currency checks
             */
            if (baseCurrency.equals("")) {
                pojo.setBase(AppVariables.CURRENCY_BASE);
            }
            else if (AvailableCurrencyTypesChecker.isThisCurrencyAvailable(baseCurrency) == false) {
                pojo.setSuccess(false);
                System.out.println("Could not find base currency in the database");
                return Optional.ofNullable(pojo);
            }
            else {
                pojo.setBase(baseCurrency);
            }


            /**
             * Get exchange rates from db
             */
            Map<String, Double> rates = new HashMap<>();
            if (pojo.getBase().equals(AppVariables.CURRENCY_BASE)) {
                for (Exchange entry: latestExchangesList
                ) {
                    rates.put(entry.getCurrency().getIsoName(), entry.getValue());
                }
            }
            else {
                if (exchangeRepository.existsByExchangeDateAndCurrency_IsoName(exchangeDate, baseCurrency)) {
                    double newRatio = calculateNewRatio(exchangeDate, baseCurrency);
                    for (Exchange entry: latestExchangesList
                    ) {
                        rates.put(entry.getCurrency().getIsoName(), entry.getValue()*newRatio);
                    }
                }
                else {
                    pojo.setSuccess(false);
                }

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

    public double calculateNewRatio(LocalDate date, String currency) {
        Exchange newBaseCurrency = exchangeRepository.findByExchangeDateAndCurrency_IsoName(date, currency);
        return 1/newBaseCurrency.getValue();
    }



    public String buildJsonFromPojo(CurrencyExchangesFromSingleDayPojo pojo) {

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

    public String buildJsonFromPojo(List<CurrencyExchangesFromSingleDayPojo> pojoList) {

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
