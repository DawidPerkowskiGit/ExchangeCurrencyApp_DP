package dpapps.exchangecurrencyapp.exchange.service;

import dpapps.exchangecurrencyapp.configuration.AppVariables;
import dpapps.exchangecurrencyapp.exchange.error.InvalidRequestBody;
import dpapps.exchangecurrencyapp.exchange.model.ApiKey;
import dpapps.exchangecurrencyapp.exchange.model.Currency;
import dpapps.exchangecurrencyapp.exchange.model.Exchange;
import dpapps.exchangecurrencyapp.exchange.model.User;
import dpapps.exchangecurrencyapp.exchange.repositories.ApiKeyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.UserRepository;
import dpapps.exchangecurrencyapp.exchange.helpers.*;
import dpapps.exchangecurrencyapp.jsonparser.response.*;
import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonConvertable;
import dpapps.exchangecurrencyapp.shedules.ScheduleJobs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    private final CurrencyRepository currencyRepository;

    private final ExchangeRepository exchangeRepository;


    private final ApiKeyRepository apiKeyRepository;

    private final UserRepository userRepository;

    private final ApiKeyService apiKeyService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ExchangeServiceImpl(CurrencyRepository currencyRepository, ExchangeRepository exchangeRepository, ApiKeyRepository apiKeyRepository, UserRepository userRepository, ApiKeyService apiKeyService) {
        this.currencyRepository = currencyRepository;
        this.exchangeRepository = exchangeRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.userRepository = userRepository;
        this.apiKeyService = apiKeyService;
    }

    /**
     * Fetched available currencies from the database
     *
     * @param date Optional parameter - currencies active at specified date
     * @return Currencies List in JSON format
     */
    public ResponseEntity<JsonConvertable> getCurrencies(String date) {
        logger.info(AppVariables.LOGGER_CURRENCIES_ENDPOINT_CALLED);
        CurrencyIsoNameToFullNameMapper currencyIsoNameToFullNameMapper = new CurrencyIsoNameToFullNameMapper();
        Iterable<Currency> currencyList;
        if (date != null) {
            LocalDate currenciesDate = LocalDateStringConverter.convertStringToLocalDate(date);
            if (currenciesDate.isEqual(AppVariables.INVALID_DATE_VALUES)) {
                currenciesDate = LocalDate.now();
            }

            LocalDate latestDate = exchangeRepository.getLatestExchangeDate();
            if (DateRangeValidator.isDateInValidRange(currenciesDate, latestDate) == false) {
                currenciesDate = DateRangeValidator.returnValidRange(currenciesDate, latestDate);
            }
            currencyList = currencyRepository.getActivelyUsedCurrenciesAtDate(currenciesDate);

        } else {
            currencyList = currencyRepository.getAll();
        }
        currencyIsoNameToFullNameMapper.convertCurrencyListToJsonCurrency(currencyList);

        return ResponseEntity.ok(currencyIsoNameToFullNameMapper);
    }

    /**
     * Fetched currencies and countries/other locations where they can be used in.
     *
     * @return Currencies list with all the countries they can be used in returned in JSON format
     */
    public ResponseEntity<List<JsonConvertable>> getCurrenciesAndLocations() {
        logger.info(AppVariables.LOGGER_CURRENCIES_LOCATIONS_ENDPOINT_CALLED);
        List<String[]> pojo = currencyRepository.getCurrenciesAndLocations();
        List<JsonConvertable> jsonReadyObject = convertDbCurrencyNameLocationToObjectList(pojo);
        return ResponseEntity.ok(jsonReadyObject);
    }


    /**
     * Admin only accessible endpoint which performs requested URL requested. It was used to perform currency exchanges import.
     *
     * @param apiKey     REST APi key
     * @param requestUrl Requested URL
     * @return status result of currency import requestapi from the scheduler
     */
    public String manualRequestUrl(String apiKey, String requestUrl) {
        int apiKeyParsingResult = apiKeyService.isApiKeyValid(apiKey);
        if (apiKeyParsingResult == AppVariables.API_KEY_INVALID) {
            return AppVariables.ERROR_BODY_API_KEY_INVALID;
        }

        if (requestUrl == null) {
            logger.info(AppVariables.LOGGER_MANUAL_REQUEST_URL_EMPTY);
            return AppVariables.LOGGER_MANUAL_REQUEST_URL_EMPTY;
        }

        ScheduleJobs scheduleJobs = new ScheduleJobs();
        return scheduleJobs.performDailyDatabaseImport(requestUrl, this.currencyRepository, this.exchangeRepository);
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
    public ResponseEntity<JsonConvertable> getExchanges(String apiKey, String currency, String baseCurrency, String startDate, String finishDate, Map<String, String> headers, String currencyValue) {
        logger.info(AppVariables.LOGGER_EXCHANGE_ENDPOINT_CALLED);
        boolean vipClientRequest = false;

        if (headers.containsKey(AppVariables.HEADER_ORIGIN)) {
            if (headers.get(AppVariables.HEADER_ORIGIN).equals(AppVariables.FRONTEND_APP_URL)) {
                vipClientRequest = true;
                logger.info(AppVariables.LOGGER_FRONTEND_APP_REQUEST);
            }
        }


        //String apiKeyParsingResult;
        ApiKey apiKeyObject;
        User user = null;
        InvalidRequestBody errorBody = new InvalidRequestBody();

        /**
         * Api Key checks
         */

        if (vipClientRequest == false) {
           int apiKeyParsingResult = apiKeyService.isApiKeyValid(apiKey);

            if (apiKeyParsingResult == AppVariables.API_KEY_NOT_PROVIDED) {
                errorBody.setStatus(AppVariables.RETURNED_JSON_BODY_FORBIDDEN);
                errorBody.setMessage(AppVariables.ERROR_BODY_API_KEY_NOT_PROVIDED);
                logger.info(AppVariables.ERROR_BODY_API_KEY_NOT_PROVIDED);
                return ResponseEntity.ok(errorBody);
            }

            if (apiKeyParsingResult == AppVariables.API_KEY_INVALID) {
                errorBody.setStatus(AppVariables.RETURNED_JSON_BODY_FORBIDDEN);
                errorBody.setMessage(AppVariables.ERROR_BODY_API_KEY_INVALID);
                logger.info(AppVariables.ERROR_BODY_API_KEY_INVALID);
                return ResponseEntity.ok(errorBody);
            }

            apiKeyObject = apiKeyRepository.findByValue(apiKey);
            user = apiKeyObject.getUser();

            switch (apiKeyService.canUseTheApiKey(apiKeyObject, user)) {
                case AppVariables.API_KEY_USE_LIMIT_REACHED -> {
                  errorBody.setStatus(403);
                  errorBody.setMessage(AppVariables.ERROR_BODY_API_KEY_USE_LIMIT_REACHED);
                  logger.warn(AppVariables.ERROR_BODY_API_KEY_USE_LIMIT_REACHED);
                  return ResponseEntity.ok(errorBody);
                }
                case AppVariables.API_KEY_INACTIVE -> {
                    errorBody.setStatus(403);
                    errorBody.setMessage(AppVariables.ERROR_BODY_API_KEY_INACTIVE);
                    logger.warn(AppVariables.ERROR_BODY_API_KEY_INACTIVE);
                    return ResponseEntity.ok(errorBody);
                }
                case AppVariables.API_KEY_ADMIN -> {
                    vipClientRequest = true;
                }
            }

        }

        /**
         * Convert String Date to LocalDate
         */

        LocalDate beginDate;
        LocalDate endDate;
        LocalDate latestExchangeDate = exchangeRepository.getLatestExchangeDate();

        /**
         * Nullable fields check, assign default values to
         */

        if (startDate == null && finishDate == null) {
            beginDate = endDate = latestExchangeDate;
        }
        else if (startDate == null) {
            beginDate = endDate = LocalDateStringConverter.convertStringToLocalDate(finishDate);
            if (endDate.isEqual(AppVariables.INVALID_DATE_VALUES)) {
                errorBody.setStatus(400);
                errorBody.setMessage("Cannot perform your request. Invalid finish date format");
                logger.warn("Cannot perform your request. Invalid finish date format");
                return ResponseEntity.ok(errorBody);
            }
        }
        else if (finishDate == null) {
            beginDate = endDate = LocalDateStringConverter.convertStringToLocalDate(startDate);
            if (beginDate.isEqual(AppVariables.INVALID_DATE_VALUES)) {
                errorBody.setStatus(400);
                errorBody.setMessage("Cannot perform your request. Invalid start date format");
                logger.warn("Cannot perform your request. Invalid start date format");
                return ResponseEntity.ok(errorBody);
            }
        }
        else {
            beginDate = LocalDateStringConverter.convertStringToLocalDate(startDate);
            if (beginDate.isEqual(AppVariables.INVALID_DATE_VALUES)) {
                errorBody.setStatus(400);
                errorBody.setMessage("Cannot perform your request. Invalid start date format");
                logger.warn("Cannot perform your request. Invalid start date format");
                return ResponseEntity.ok(errorBody);
            }
            endDate = LocalDateStringConverter.convertStringToLocalDate(finishDate);
            if (endDate.isEqual(AppVariables.INVALID_DATE_VALUES)) {
                errorBody.setStatus(400);
                errorBody.setMessage("Cannot perform your request. Invalid finish date format");
                logger.warn("Cannot perform your request. Invalid finish date format");
                return ResponseEntity.ok(errorBody);

            }
        }


        List<String> requestedCurernciesList = new LinkedList<>();

        if (currency == null) {
            requestedCurernciesList.add(AppVariables.ALL_CURRENCIES);
        } else {
            requestedCurernciesList = CurrencyListExtractor.extractCurrencyList(currency);
            if (requestedCurernciesList.isEmpty()) {
                errorBody.setStatus(404);
                errorBody.setMessage("Cannot perform your request. Requested currency is not found");
                logger.warn("Cannot perform your request. Requested currency is not found");
                return ResponseEntity.ok(errorBody);
            }
        }

        if (baseCurrency == null) {
            baseCurrency = AppVariables.DEFAULT_BASE_CURRENCY;
        } else if (AvailableCurrencyTypesChecker.isThisCurrencyAvailable(baseCurrency) == false) {
            errorBody.setStatus(404);
            errorBody.setMessage("Cannot perform your request. Base currency is not found");
            logger.warn("Cannot perform your request. Base currency is not found");
            return ResponseEntity.ok(errorBody);
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


        /**
         * Check if requested exchange rates are form a single day, if they, are return exchanges form the day.
         * If the exchange rates are from multiple days, return list of exchanges.
         */
        List<JsonConvertable> returnList = getExchangesFromMultipleDays(beginDate, endDate, requestedCurernciesList, baseCurrency);


        if (returnList.isEmpty()) {
            errorBody.setStatus(500);
            errorBody.setMessage("Failed to return exchange rates. There is no data matching your request criteria");
            logger.warn("Failed to return exchange rates. There is no data matching your request criteria");
            return ResponseEntity.ok(errorBody);
        }

        /**
         * Check if currency value is defined. If it is, return exchange rate requested value.
         */
        if (currencyValue != null && beginDate.equals(endDate) && requestedCurernciesList.size()==1) {
            if (StringToNumericConverter.isStringValidDouble(currencyValue)) {
                Double currencyExchangeValue = StringToNumericConverter.convertStringToDouble(currencyValue);
                CurrencyConverterReturnedBody currencyConverterReturnedBody = new CurrencyConverterReturnedBody();
                currencyConverterReturnedBody.setRequestedValue(currencyExchangeValue);
                currencyConverterReturnedBody.setBaseCurrency(baseCurrency);
                currencyConverterReturnedBody.setRequestedCurrency(requestedCurernciesList.get(0));
                currencyConverterReturnedBody.setExchangeDate(beginDate);

                SingleDayExchangeRatesJson singleDayExchangeRatesJson = (SingleDayExchangeRatesJson) returnList.get(0);
                currencyConverterReturnedBody.setRate(singleDayExchangeRatesJson.getRates().get(requestedCurernciesList.get(0)));
                currencyConverterReturnedBody.calculateValue();
                currencyConverterReturnedBody.composeMessage();
                return ResponseEntity.ok(currencyConverterReturnedBody);
            }
        }


        if (vipClientRequest == false) {
            user.setCurrentRequestsCount(user.getCurrentRequestsCount() + 1);
            userRepository.save(user);
        }

        ExchangesList exchangesList = new ExchangesList();
        exchangesList.setExchangeList(returnList);

        return ResponseEntity.ok(exchangesList);
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
    public List<JsonConvertable> getExchangesFromMultipleDays(LocalDate beginDate, LocalDate endDate, List<String> currency, String baseCurrency) {


        List<JsonConvertable> exchangeList = new ArrayList<>();

        LocalDate currentDate = beginDate;
        while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
            SingleDayExchangeRatesJson singleDayExchanges = (SingleDayExchangeRatesJson) getExchangesFromSingleDay(currentDate, currency, baseCurrency);

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
    public JsonConvertable getExchangesFromSingleDay(LocalDate inputDate, List<String> currency, String baseCurrency) {


        SingleDayExchangeRatesJson pojo = new SingleDayExchangeRatesJson();
        LocalDate exchangeDate = inputDate;
        InvalidRequestBody invalidRequestBody = new InvalidRequestBody();

        if (exchangeRepository.existsByExchangeDate(exchangeDate) == false) {
            pojo.setSuccess(false);
            return pojo;
        }
        /**
         * Fetch exchanges from database
         */
        try {
            List<Exchange> latestExchangesList = new LinkedList<>();

            if (currency.get(0).equals(AppVariables.ALL_CURRENCIES)) {
                latestExchangesList = exchangeRepository.findAllByExchangeDateOrderByCurrencyDesc(exchangeDate);
            } else {
                latestExchangesList = exchangeRepository.findAllByExchangeDateAndCurrenciesOrderByExchangeDate(exchangeDate, currency);
            }


            /**
             * Base currency checks
             */
            if (baseCurrency.equals("")) {
                pojo.setBase(AppVariables.DEFAULT_BASE_CURRENCY);
            } else if (AvailableCurrencyTypesChecker.isThisCurrencyAvailable(baseCurrency) == false) {
                invalidRequestBody.setStatus(404);
                invalidRequestBody.setMessage("Could not find base currency in the database");
                logger.warn("Could not find base currency in the database");
                return invalidRequestBody;
            } else {
                pojo.setBase(baseCurrency);
            }


            /**
             * Get exchange rates from db
             */
            Map<String, Double> rates = new HashMap<>();
            if (pojo.getBase().equals(AppVariables.DEFAULT_BASE_CURRENCY)) {
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
                    invalidRequestBody.setStatus(404);
                    invalidRequestBody.setMessage("Could not find exchange rates from specified date and currency type");
                    logger.warn("Could not find exchange rates from specified date and currency type");
                    return invalidRequestBody;
                }

            }

            pojo.setDate(exchangeDate);
            pojo.setRates(rates);
            pojo.setSuccess(true);
        } catch (Exception e) {
            logger.error("Could not acquire latest exchange data: Exception: " + e);
        }
        return pojo;
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
     * Converts returned database rows to object ready for serialization
     *
     * @param databaseEntries entire retrieved from the database, structure [iso_name, full_name, location]
     * @return Object of structure [iso_name, full_name, List<location>] ready for serialization
     */
    public List<JsonConvertable> convertDbCurrencyNameLocationToObjectList(Iterable<String[]> databaseEntries) {

        Map<String, String> isoNameFullName = new HashMap();
        Map<String, List<String>> isoNameLocation = new HashMap();

        for (String[] singleEntry : databaseEntries) {
            if (isoNameFullName.containsKey(singleEntry[0]) == false) {
                isoNameFullName.put(singleEntry[0], singleEntry[1]);
            }
            if (isoNameLocation.containsKey(singleEntry[0]) == false) {
                isoNameLocation.put(singleEntry[0], new ArrayList<>());
            }
            isoNameLocation.put(singleEntry[0], addValueReplaceMap(singleEntry[2], isoNameLocation.get(singleEntry[0])));

        }

        List<JsonConvertable> pojoToSerialize = new ArrayList<>();

        for (String entry : isoNameLocation.keySet()) {
            CurrencyIsoNameFullNameMultipleLocations singleObject = new CurrencyIsoNameFullNameMultipleLocations();
            singleObject.setIsoName(entry);
            singleObject.setFullName(isoNameFullName.get(entry));
            singleObject.setLocationList(isoNameLocation.get(entry));
            pojoToSerialize.add(singleObject);
        }

        return pojoToSerialize;
    }

    /**
     * Adds entry to List property of a HashMap
     *
     * @param value Value needed to be added to the List
     * @param list  List of entries
     * @return List with an additional value
     */
    public List<String> addValueReplaceMap(String value, List<String> list) {
        list.add(value);
        return list;
    }


}
