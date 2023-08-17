package dpapps.exchangecurrencyapp.exchange.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dpapps.exchangecurrencyapp.configuration.AppVariables;
import dpapps.exchangecurrencyapp.exchange.entities.ApiKey;
import dpapps.exchangecurrencyapp.exchange.entities.Currency;
import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.entities.User;
import dpapps.exchangecurrencyapp.exchange.repositories.*;
import dpapps.exchangecurrencyapp.exchange.services.ApiKeyManager;
import dpapps.exchangecurrencyapp.exchange.tools.AvailableCurrencyTypesChecker;
import dpapps.exchangecurrencyapp.exchange.tools.ConversionLocalDateString;
import dpapps.exchangecurrencyapp.exchange.tools.DateRange;
import dpapps.exchangecurrencyapp.jsonparser.responsepojo.*;
import dpapps.exchangecurrencyapp.shedules.ScheduleJobs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Controller defining all REST API endpoints
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ResponseApiController {
    CurrencyRepository currencyRepository;

    ExchangeRepository exchangeRepository;

    LocationRepository locationRepository;

    LocationCurrencyPairRepository locationCurrencyPairRepository;
    private final ApiKeyRepository apiKeyRepository;

    private final UserRepository userRepository;

    @Autowired
    public ResponseApiController(CurrencyRepository currencyRepository,
                                 ExchangeRepository exchangeRepository,
                                 LocationRepository locationRepository,
                                 LocationCurrencyPairRepository locationCurrencyPairRepository,
                                 ApiKeyRepository apiKeyRepository,
                                 UserRepository userRepository,
                                 RoleRepository roleRepository) {
        this.currencyRepository = currencyRepository;
        this.exchangeRepository = exchangeRepository;
        this.locationRepository = locationRepository;
        this.locationCurrencyPairRepository = locationCurrencyPairRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.userRepository = userRepository;
    }

    /**
     * Currencies List
     *
     * @return Currencies List in JSON format
     */

    @GetMapping("/currencies")
    public String getCurrencies() {
        System.out.println("api/currencies called");
        CurrenciesListPojo currencyListPojo = new CurrenciesListPojo();
        Iterable<Currency> currencyList = currencyRepository.getAll();
        currencyListPojo.convertCurrencyListToJsonCurrency(currencyList);

        return buildJsonFromPojo(currencyListPojo);
    }

    /**
     * Currencies and Countries where they can be used
     *
     * @return Currencies list with all the countries they can be used in returned in JSON format
     */

    @GetMapping("/currencies/locations")
    public String getCurrenciesAndLocations() {
        System.out.println("api/currandloc called");
        List<String[]> pojo = currencyRepository.getCurrenciesAndLocations();
        System.out.println(pojo);
        CurrencyNamesLocationObjectJsonReadyListCreator rowsToObjectConverter = new CurrencyNamesLocationObjectJsonReadyListCreator();
        List<JsonConvertable> jsonReadyObject = rowsToObjectConverter.packCurrenciesLocationArrayToObject(pojo);
        System.out.println(jsonReadyObject);
        String returnedJson = buildJsonFromPojo(jsonReadyObject);
        return returnedJson;
    }

    /**
     * Admin only accessible endpoint which performs requested URL requested. It was used to perform currency exchanges import.
     *
     * @param apiKey     REST APi key
     * @param requestUrl Requested URL
     * @return status result of currency import request from the scheduler
     */
    @GetMapping("/forceRequestUrl")
    public String forceRequestUrl(@RequestParam(required = false) String apiKey,
                                  @RequestParam(required = false) String requestUrl) {

        String apiKeyParsingResult = checkApiKey(apiKey);
        if (!apiKeyParsingResult.equals(AppVariables.VALID_API_KEY_MESSAGE)) {
            return apiKeyParsingResult;
        }

        if (requestUrl == null) {
            return "request Url is null";
        }

        ScheduleJobs scheduleJobs = new ScheduleJobs();

        return scheduleJobs.performDailyDatabaseImport(requestUrl, this.currencyRepository, this.exchangeRepository);


    }

    /**
     * Method that checks if API key is valid
     *
     * @param apiKey API key
     * @return result of API key check
     */
    private String checkApiKey(String apiKey) {
        if (apiKey == null) {
            return "You didnt provide api key";
        }

        if (apiKeyRepository.existsByValue(apiKey) == false) {
            return "Api key is invalid";
        }

        return AppVariables.VALID_API_KEY_MESSAGE;
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
    public String getExchangeRates(@RequestParam(required = false) String currency,
                                   @RequestParam(required = false) String startDate,
                                   @RequestParam(required = false) String finishDate,
                                   @RequestParam(required = false) String baseCurrency,
                                   @RequestParam(required = false) String apiKey,
                                   @RequestHeader Map<String, String> headers) {

        System.out.println("entered exchange");

        headers.forEach((key, value) -> {
            System.out.printf("Header '%s' = %s%n", key, value);
        });

        boolean vipClientRequest = false;

        if (headers.containsKey("origin")) {
            if (headers.get("origin").equals(AppVariables.FRONTEND_APP_URL)) {
                vipClientRequest = true;
                System.out.println("Api request by Frontend app client");
            }
        }


        String apiKeyParsingResult = "";
        ApiKey apiKeyObject;
        User user = null;

        /**
         * Api Key checks
         */

        if (vipClientRequest == false) {
            apiKeyParsingResult = checkApiKey(apiKey);
            if (!apiKeyParsingResult.equals(AppVariables.VALID_API_KEY_MESSAGE)) {
                return apiKeyParsingResult;
            }

            apiKeyObject = apiKeyRepository.findByValue(apiKey);

            user = apiKeyObject.getUser();
            ApiKeyManager apiKeyManager = new ApiKeyManager(user, apiKeyRepository);
            if (apiKeyManager.canUseTheApiKey(apiKeyObject) == false) {
                return "Cannot perform API request";
            }
        }


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
        LocalDate onesDate = LocalDate.of(1, 1, 1);

        /**
         * Check if parameters are empty
         */

        if (beginDate.isEqual(onesDate) && endDate.isEqual(onesDate)) {
            beginDate = endDate = LocalDate.now();
        }
        if (beginDate.isEqual(onesDate)) {
            beginDate = endDate;
        } else if (endDate.isEqual(onesDate)) {
            endDate = beginDate;
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
        List<JsonConvertable> pojoList = new ArrayList<>();

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
        } else {
            pojoList = getExchangesFromMultipleDays(beginDate, endDate, currency, baseCurrency);
            returnedJson = buildJsonFromPojo(pojoList);
        }
//        int numberOfUsages = userRepository.howManyUsages(user.getId()) + 1;
//        userRepository.increaseNumberOfUsages(user.getId(), numberOfUsages);
        if (vipClientRequest == false) {
            user.setCurrentRequestsCount(user.getCurrentRequestsCount() + 1);
            userRepository.save(user);
        }

        return returnedJson;
    }

    /**
     * Method which returns exchange rates from multiple days. It is called when accessing "../exchange" endpoint
     *
     * @param beginDate    Exchange rates starting from date
     * @param endDate      Exchange rates ending in date
     * @param currency     Requested currency
     * @param baseCurrency Base currency
     * @return List of exchange rates
     */
    public List<JsonConvertable> getExchangesFromMultipleDays(LocalDate beginDate,
                                                              LocalDate endDate,
                                                              String currency,
                                                              String baseCurrency) {

        List<JsonConvertable> exchangeList = new ArrayList<>();

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

    /**
     * Exchange rates from single day. It is called at least once when returning exchange rates from multiple days
     *
     * @param inputDate    Exchange rates date
     * @param currency     Requested currency
     * @param baseCurrency Base currency
     * @return Object containing exchange rated from single day
     */
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
            } else if (AvailableCurrencyTypesChecker.isThisCurrencyAvailable(currency)) {
                latestExchangesList = exchangeRepository.findAllByExchangeDateAndCurrencyOrderByExchangeDate(exchangeDate, currency);
            } else {
                pojo.setSuccess(false);
                return Optional.ofNullable(pojo);
            }


            /**
             * Base currency checks
             */
            if (baseCurrency.equals("")) {
                pojo.setBase(AppVariables.CURRENCY_BASE);
            } else if (AvailableCurrencyTypesChecker.isThisCurrencyAvailable(baseCurrency) == false) {
                pojo.setSuccess(false);
                System.out.println("Could not find base currency in the database");
                return Optional.ofNullable(pojo);
            } else {
                pojo.setBase(baseCurrency);
            }


            /**
             * Get exchange rates from db
             */
            Map<String, Double> rates = new HashMap<>();
            if (pojo.getBase().equals(AppVariables.CURRENCY_BASE)) {
                for (Exchange entry : latestExchangesList
                ) {
                    rates.put(entry.getCurrency().getIsoName(), entry.getValue());
                }
            } else {
                if (exchangeRepository.existsByExchangeDateAndCurrency_IsoName(exchangeDate, baseCurrency)) {
                    double newRatio = calculateNewRatio(exchangeDate, baseCurrency);
                    for (Exchange entry : latestExchangesList
                    ) {
                        rates.put(entry.getCurrency().getIsoName(), entry.getValue() * newRatio);
                    }
                } else {
                    pojo.setSuccess(false);
                }

            }

            pojo.setDate(exchangeDate);
            pojo.setRates(rates);
            pojo.setSuccess(true);
        } catch (Exception e) {
            System.out.println("Could not acquire latest exchange data: Exception: " + e);
        }
        return Optional.ofNullable(pojo);
    }

    /**
     * Method which returns exchange rate based on non-default currency
     *
     * @param date     Exchange rates date
     * @param currency Currency used as base
     * @return Exchange rate based on requested currency
     */

    public double calculateNewRatio(LocalDate date, String currency) {
        Exchange newBaseCurrency = exchangeRepository.findByExchangeDateAndCurrency_IsoName(date, currency);
        return 1 / newBaseCurrency.getValue();
    }


    /**
     * Returns list of JSON-parseable objects in JSON format
     *
     * @param pojoList Java List object containing Exchange or Currency data
     * @return Object list in JSON format
     */
    public String buildJsonFromPojo(List<JsonConvertable> pojoList) {
        ObjectMapper objectMapper = new ObjectMapper();
        String exchangesToJson = "";

        if (pojoList.size() == 1) {
            try {
                exchangesToJson = objectMapper.writeValueAsString(pojoList.get(0));
            } catch (Exception e) {
                System.out.println("Could not map object to JSON. Exception: " + e);
            }
        } else {
            try {
                exchangesToJson = objectMapper.writeValueAsString(pojoList);
            } catch (Exception e) {
                System.out.println("Could not map object to JSON. Exception: " + e);
            }
        }

        return exchangesToJson;
    }

    /**
     * Returns an Object in JSON format
     *
     * @param pojoElement Java object containing Exchange or Currency data
     * @return Requested data in JSON format
     */

    public String buildJsonFromPojo(JsonConvertable pojoElement) {

        List<JsonConvertable> list = new ArrayList<>();
        list.add(pojoElement);
        return buildJsonFromPojo(list);
    }

}
