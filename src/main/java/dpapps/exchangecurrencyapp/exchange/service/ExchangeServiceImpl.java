package dpapps.exchangecurrencyapp.exchange.service;

import dpapps.exchangecurrencyapp.configuration.AppVariables;
import dpapps.exchangecurrencyapp.exchange.model.ApiKey;
import dpapps.exchangecurrencyapp.exchange.model.Currency;
import dpapps.exchangecurrencyapp.exchange.model.Exchange;
import dpapps.exchangecurrencyapp.exchange.model.User;
import dpapps.exchangecurrencyapp.exchange.repositories.*;
import dpapps.exchangecurrencyapp.exchange.tools.AvailableCurrencyTypesChecker;
import dpapps.exchangecurrencyapp.exchange.tools.LocalDateStringConverter;
import dpapps.exchangecurrencyapp.exchange.tools.DateRangeValidator;
import dpapps.exchangecurrencyapp.jsonparser.entity.CurrencyEntityListToMap;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.CurrencyNamesLocationToJsonConverter;
import dpapps.exchangecurrencyapp.jsonparser.entity.JsonConvertable;
import dpapps.exchangecurrencyapp.jsonparser.entity.SingleDayExchangeRatesJson;
import dpapps.exchangecurrencyapp.shedules.ScheduleJobs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ExchangeServiceImpl implements ExchangeService{

    private final CurrencyRepository currencyRepository;

    private final ExchangeRepository exchangeRepository;

    private final LocationRepository locationRepository;

    private final LocationCurrencyPairRepository locationCurrencyPairRepository;
    private final ApiKeyRepository apiKeyRepository;

    private final UserRepository userRepository;

    private final ApiKeyService apiKeyService;

    private final JsonBuilderService jsonBuilderService;


    @Autowired
    public ExchangeServiceImpl(CurrencyRepository currencyRepository, ExchangeRepository exchangeRepository, LocationRepository locationRepository, LocationCurrencyPairRepository locationCurrencyPairRepository, ApiKeyRepository apiKeyRepository, UserRepository userRepository, RoleRepository roleRepository, ApiKeyService apiKeyService, JsonBuilderService jsonBuilderService) {
        this.currencyRepository = currencyRepository;
        this.exchangeRepository = exchangeRepository;
        this.locationRepository = locationRepository;
        this.locationCurrencyPairRepository = locationCurrencyPairRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.userRepository = userRepository;
        this.apiKeyService = apiKeyService;
        this.jsonBuilderService = jsonBuilderService;
    }

    public String getCurrencies(String date) {
        System.out.println("api/currencies called");
        CurrencyEntityListToMap currencyEntityListToMap = new CurrencyEntityListToMap();
        Iterable<Currency> currencyList;
        if (date != null) {
            LocalDate currenciesDate = LocalDateStringConverter.convertStringToLocalDate(date);
            LocalDate onesDate = LocalDate.of(1, 1, 1);
            if (currenciesDate.isEqual(onesDate)) {
                currenciesDate = LocalDate.now();
            }

            LocalDate latestDate = exchangeRepository.getLatestExchangeDate();
            if (DateRangeValidator.isDateInValidRange(currenciesDate, latestDate) == false) {
                currenciesDate = DateRangeValidator.returnValidRange(currenciesDate, latestDate);
            }
            currencyList = currencyRepository.getActivelyUsedCurrenciesAtDate(currenciesDate);

        }
        else {
            currencyList = currencyRepository.getAll();
        }
        currencyEntityListToMap.convertCurrencyListToJsonCurrency(currencyList);

        return jsonBuilderService.buildJsonFromPojo(currencyEntityListToMap);
    }

    public String getCurrenciesAndLocations() {
        System.out.println("api/currandloc called");
        List<String[]> pojo = currencyRepository.getCurrenciesAndLocations();
        System.out.println(pojo);
        CurrencyNamesLocationToJsonConverter rowsToObjectConverter = new CurrencyNamesLocationToJsonConverter();
        List<JsonConvertable> jsonReadyObject = rowsToObjectConverter.convert(pojo);
        System.out.println(jsonReadyObject);
        String returnedJson = jsonBuilderService.buildJsonFromPojo(jsonReadyObject);
        return returnedJson;
    }

    public String manualRequestUrl(String apiKey, String requestUrl) {
        String apiKeyParsingResult = apiKeyService.checkApiKey(apiKey);
        if (!apiKeyParsingResult.equals(AppVariables.VALID_API_KEY_MESSAGE)) {
            return apiKeyParsingResult;
        }

        if (requestUrl == null) {
            return "request Url is null";
        }

        ScheduleJobs scheduleJobs = new ScheduleJobs();

        return scheduleJobs.performDailyDatabaseImport(requestUrl, this.currencyRepository, this.exchangeRepository);
    }

    public String getExchanges(String currency, String startDate, String finishDate, String baseCurrency, String apiKey, Map<String, String> headers) {
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
            apiKeyParsingResult = apiKeyService.checkApiKey(apiKey);
            if (!apiKeyParsingResult.equals(AppVariables.VALID_API_KEY_MESSAGE)) {
                return apiKeyParsingResult;
            }

            apiKeyObject = apiKeyRepository.findByValue(apiKey);

            user = apiKeyObject.getUser();
            if (apiKeyService.canUseTheApiKey(apiKeyObject, user) == false) {
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
        LocalDate beginDate = LocalDateStringConverter.convertStringToLocalDate(startDate);
        LocalDate endDate = LocalDateStringConverter.convertStringToLocalDate(finishDate);
        LocalDate onesDate = LocalDate.of(1, 1, 1);

        /**
         * Check if parameters are empty
         */

        if (beginDate.isEqual(onesDate) && endDate.isEqual(onesDate)) {
            beginDate = endDate = LocalDate.now();
            //beginDate = endDate = exchangeRepository.getLatestExchangeDate();
        }
        if (beginDate.isEqual(onesDate)) {
            beginDate = endDate;
        } else if (endDate.isEqual(onesDate)) {
            endDate = beginDate;
        }


        /**
         * Check if dates are in valid range, if they are not, return correct date range.
         */
        LocalDate latestDate = exchangeRepository.getLatestExchangeDate();

        if (DateRangeValidator.isDateInValidRange(beginDate, endDate, latestDate) == false) {
            LocalDate[] beginEndDate = DateRangeValidator.returnValidRange(beginDate, endDate, latestDate);
            beginDate = beginEndDate[0];
            endDate = beginEndDate[1];
        }


        Optional<SingleDayExchangeRatesJson> pojo = Optional.empty();
        String returnedJson = "";
        List<JsonConvertable> pojoList = new ArrayList<>();

        /**
         * Check if requested exchange rates are form a single day, if they, are return exchanges form the day.
         * If the exchange rates are from multiple days, return list of exchanges.
         */

//        if (beginDate.isEqual(endDate)) {
//            pojo = getExchangesFromSingleDay(beginDate, currency, baseCurrency);
//            if (pojo.isEmpty()) {
//                return "Failed to return exchange rates";
//            }
//
//            returnedJson = jsonBuilderService.buildJsonFromPojo(pojo.get());
//
//            if (returnedJson.equals("")) {
//                return "Json body is empty";
//            }
//        } else {
//            pojoList = getExchangesFromMultipleDays(beginDate, endDate, currency, baseCurrency);
//            returnedJson = jsonBuilderService.buildJsonFromPojo(pojoList);
//        }



        pojoList = getExchangesFromMultipleDays(beginDate, endDate, currency, baseCurrency);


        if (pojoList.isEmpty()) {
            return "Failed to return exchange rates";
        }

        returnedJson = jsonBuilderService.buildJsonFromPojo(pojoList);

        if (returnedJson.equals("")) {
            return "Json body is empty";
        }

        if (vipClientRequest == false) {
            user.setCurrentRequestsCount(user.getCurrentRequestsCount() + 1);
            userRepository.save(user);
        }

        return returnedJson;
    }

    /**
     * Returns exchange rates from multiple days. It is called when accessing "../exchange" endpoint
     *
     * @param beginDate    Exchange rates starting from date
     * @param endDate      Exchange rates ending in date
     * @param currency     Requested currency
     * @param baseCurrency Base currency
     * @return List of exchange rates
     */
    public List<JsonConvertable> getExchangesFromMultipleDays(LocalDate beginDate, LocalDate endDate, String currency, String baseCurrency) {

        List<JsonConvertable> exchangeList = new ArrayList<>();

        LocalDate currentDate = beginDate;
        while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
            SingleDayExchangeRatesJson singleDayExchanges = getExchangesFromSingleDay(currentDate, currency, baseCurrency).get();
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
    public Optional<SingleDayExchangeRatesJson> getExchangesFromSingleDay(LocalDate inputDate, String currency, String baseCurrency) {


        SingleDayExchangeRatesJson pojo = new SingleDayExchangeRatesJson();

        LocalDate exchangeDate = inputDate;
//        /**
//         * Exchange date checks
//         */
//        try {
//            if (DateRangeValidator.isDateInValidRange(inputDate)==false) {
//                exchangeDate = DateRangeValidator.returnValidRange(inputDate);
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
                for (Exchange entry : latestExchangesList) {
                    rates.put(entry.getCurrency().getIsoName(), entry.getValue());
                }
            } else {
                if (exchangeRepository.existsByExchangeDateAndCurrency_IsoName(exchangeDate, baseCurrency)) {
                    double newRatio = calculateNewRatio(exchangeDate, baseCurrency);
                    for (Exchange entry : latestExchangesList) {
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


}
