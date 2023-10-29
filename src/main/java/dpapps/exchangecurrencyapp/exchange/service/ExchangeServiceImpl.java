package dpapps.exchangecurrencyapp.exchange.service;

import dpapps.exchangecurrencyapp.configuration.AppConstants;
import dpapps.exchangecurrencyapp.exchange.error.InvalidRequestBody;
import dpapps.exchangecurrencyapp.exchange.helpers.*;
import dpapps.exchangecurrencyapp.exchange.model.ApiKey;
import dpapps.exchangecurrencyapp.exchange.model.Currency;
import dpapps.exchangecurrencyapp.exchange.model.Exchange;
import dpapps.exchangecurrencyapp.exchange.model.User;
import dpapps.exchangecurrencyapp.exchange.repositories.ApiKeyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.UserRepository;
import dpapps.exchangecurrencyapp.jsonparser.response.CurrencyConversionReturnedObject;
import dpapps.exchangecurrencyapp.jsonparser.response.CurrencyIsoFullNamesReturnedObject;
import dpapps.exchangecurrencyapp.jsonparser.response.model.CurrencyIsoNameFullNameMultipleLocations;
import dpapps.exchangecurrencyapp.jsonparser.response.model.ExchangesList;
import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonConvertable;
import dpapps.exchangecurrencyapp.jsonparser.response.model.SingleDayExchangeRatesJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
     * Builds and returns JSON body when currency conversion is required
     */
    private static CurrencyConversionReturnedObject buildCurrencyConversionBody(String baseCurrency, String currencyValue, LocalDate date, List<String> requestedCurrenciesList, List<JsonConvertable> exchangesList) {
        Double requestedValueAsDouble = StringIntConverter.convertStringToDouble(currencyValue);
        SingleDayExchangeRatesJson singleDayExchangeRatesJson = (SingleDayExchangeRatesJson) exchangesList.get(0);
        Double rate = singleDayExchangeRatesJson.getRates().get(requestedCurrenciesList.get(0));
        String requestedCurrency = requestedCurrenciesList.get(0);

        return new CurrencyConversionReturnedObject(date, requestedValueAsDouble, rate, baseCurrency, requestedCurrency);
    }

    public ResponseEntity<JsonConvertable> getCurrencies(String date) {
        logger.info(AppConstants.LOGGER_CURRENCIES_ENDPOINT_CALLED);
        CurrencyIsoFullNamesReturnedObject currencyIsoFullNamesReturnedObject = new CurrencyIsoFullNamesReturnedObject();
        Iterable<Currency> currencyList;
        if (date != null) {
            LocalDate currenciesDate = DateStringConverter.convertStringToLocalDate(date);
            if (currenciesDate.isEqual(AppConstants.INVALID_DATE_VALUES)) {
                currenciesDate = LocalDate.now();
            }

            LocalDate latestDate = exchangeRepository.getLatestExchangeDate();
            if (!DateRangeValidator.isDateInValidRange(currenciesDate, latestDate)) {
                currenciesDate = DateRangeValidator.returnValidRange(currenciesDate, latestDate);
            }
            currencyList = currencyRepository.getActivelyUsedCurrenciesAtDate(currenciesDate);

        } else {
            currencyList = currencyRepository.getAll();
        }
        currencyIsoFullNamesReturnedObject.convertCurrencyListToJsonCurrency(currencyList);

        return ResponseEntity.ok(currencyIsoFullNamesReturnedObject.getCurrenciesMap());
    }

    public ResponseEntity<List<JsonConvertable>> getCurrenciesAndLocations() {
        logger.info(AppConstants.LOGGER_CURRENCIES_LOCATIONS_ENDPOINT_CALLED);
        List<String[]> currenciesAndLocations = currencyRepository.getCurrenciesAndLocations();
        List<JsonConvertable> jsonReadyObject = prepareCurrencyLocationsResponseBody(currenciesAndLocations);
        return ResponseEntity.ok(jsonReadyObject);
    }

    /**
     * Converts returned database rows each containing single currency name entry and single location entry to Object containing single currency name and List of locations
     */
    private List<JsonConvertable> prepareCurrencyLocationsResponseBody(Iterable<String[]> databaseEntries) {

        Map<String, String> isoNameFullName = new HashMap<>();
        Map<String, List<String>> isoNameLocation = new HashMap<>();

        for (String[] singleEntry : databaseEntries) {
            if (!isoNameFullName.containsKey(singleEntry[0])) {
                isoNameFullName.put(singleEntry[0], singleEntry[1]);
            }
            if (!isoNameLocation.containsKey(singleEntry[0])) {
                isoNameLocation.put(singleEntry[0], new ArrayList<>());
            }
            isoNameLocation.put(singleEntry[0], addValueToList(singleEntry[2], isoNameLocation.get(singleEntry[0])));

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
     * Adds entry to List
     */
    private List<String> addValueToList(String value, List<String> list) {
        list.add(value);
        return list;
    }

    public ResponseEntity<JsonConvertable> getExchanges(String apiKey, String currency, String baseCurrency, String startDate, String finishDate, Map<String, String> headers, String currencyValue) {
        logger.info(AppConstants.LOGGER_EXCHANGE_ENDPOINT_CALLED);
        boolean vipClientRequest = false;

        if (headers.containsKey(AppConstants.HEADER_ORIGIN)) {
            if (headers.get(AppConstants.HEADER_ORIGIN).equals(AppConstants.FRONTEND_APP_URL)) {
                vipClientRequest = true;
                logger.info(AppConstants.LOGGER_FRONTEND_APP_REQUEST);
            }
        }

        ApiKey apiKeyObject;
        User user = null;


        if (!vipClientRequest) {
            int apiKeyParsingResult = apiKeyService.doesKeyExist(apiKey);

            if (apiKeyParsingResult == AppConstants.API_KEY_NOT_PROVIDED) {
                return ResponseEntity.ok(buildInvalidRequestBody(AppConstants.RETURNED_JSON_BODY_FORBIDDEN, AppConstants.ERROR_BODY_API_KEY_NOT_PROVIDED));
            }

            if (apiKeyParsingResult == AppConstants.API_KEY_INVALID) {
                return ResponseEntity.ok(buildInvalidRequestBody(AppConstants.RETURNED_JSON_BODY_FORBIDDEN, AppConstants.ERROR_BODY_API_KEY_INVALID));
            }

            apiKeyObject = apiKeyRepository.findByValue(apiKey);
            user = apiKeyObject.getUser();

            switch (apiKeyService.canUseTheApiKey(apiKeyObject, user)) {
                case AppConstants.API_KEY_USE_LIMIT_REACHED -> {
                    return ResponseEntity.ok(buildInvalidRequestBody(AppConstants.RETURNED_JSON_BODY_FORBIDDEN, AppConstants.ERROR_BODY_API_KEY_USE_LIMIT_REACHED));
                }
                case AppConstants.API_KEY_INACTIVE -> {
                    return ResponseEntity.ok(buildInvalidRequestBody(AppConstants.RETURNED_JSON_BODY_FORBIDDEN, AppConstants.ERROR_BODY_API_KEY_INACTIVE));
                }
                case AppConstants.API_KEY_ADMIN -> {
                    vipClientRequest = true;
                }
            }

        }


        LocalDate beginDate;
        LocalDate endDate;
        LocalDate latestExchangeDate = exchangeRepository.getLatestExchangeDate();


        if (startDate == null && finishDate == null) {
            beginDate = endDate = latestExchangeDate;
        } else if (startDate == null) {
            beginDate = endDate = DateStringConverter.convertStringToLocalDate(finishDate);
            if (endDate.isEqual(AppConstants.INVALID_DATE_VALUES)) {
                return ResponseEntity.ok(buildInvalidRequestBody(AppConstants.RETURNED_JSON_BODY_BAD_REQUEST, AppConstants.ERROR_BODY_INVALID_FINISH_DATE));
            }
        } else if (finishDate == null) {
            beginDate = endDate = DateStringConverter.convertStringToLocalDate(startDate);
            if (beginDate.isEqual(AppConstants.INVALID_DATE_VALUES)) {
                return ResponseEntity.ok(buildInvalidRequestBody(AppConstants.RETURNED_JSON_BODY_BAD_REQUEST, AppConstants.ERROR_BODY_INVALID_START_DATE));
            }
        } else {
            beginDate = DateStringConverter.convertStringToLocalDate(startDate);
            if (beginDate.isEqual(AppConstants.INVALID_DATE_VALUES)) {
                return ResponseEntity.ok(buildInvalidRequestBody(AppConstants.RETURNED_JSON_BODY_BAD_REQUEST, AppConstants.ERROR_BODY_INVALID_START_DATE));

            }
            endDate = DateStringConverter.convertStringToLocalDate(finishDate);
            if (endDate.isEqual(AppConstants.INVALID_DATE_VALUES)) {
                return ResponseEntity.ok(buildInvalidRequestBody(AppConstants.RETURNED_JSON_BODY_BAD_REQUEST, AppConstants.ERROR_BODY_INVALID_FINISH_DATE));
            }
        }

        List<String> requestedCurernciesList = new LinkedList<>();

        if (currency == null) {
            requestedCurernciesList.add(AppConstants.ALL_CURRENCIES);
        } else {
            requestedCurernciesList = CurrencyListParser.parseCurrencyList(currency);
            if (requestedCurernciesList.isEmpty()) {
                return ResponseEntity.ok(buildInvalidRequestBody(AppConstants.RETURNED_JSON_BODY_NOT_FOUND, AppConstants.ERROR_BODY_INVALID_REQUESTED_CURRENCY));
            }
        }

        if (baseCurrency == null) {
            baseCurrency = AppConstants.DEFAULT_BASE_CURRENCY;
        } else if (!CurrencyTypesValidator.isThisCurrencyAvailable(baseCurrency)) {
            return ResponseEntity.ok(buildInvalidRequestBody(AppConstants.RETURNED_JSON_BODY_NOT_FOUND, AppConstants.ERROR_BODY_INVALID_BASE_CURRENCY));
        }


        LocalDate latestDate = exchangeRepository.getLatestExchangeDate();

        if (!DateRangeValidator.areDatesInValidRange(beginDate, endDate, latestDate)) {
            LocalDate[] validatedDates = DateRangeValidator.returnValidRange(beginDate, endDate, latestDate);
            beginDate = validatedDates[0];
            endDate = validatedDates[1];
        }

        long diffInDays = ChronoUnit.DAYS.between(beginDate, endDate);

        if (diffInDays > AppConstants.MAXIMUM_DATE_RANGE_IN_DAYS && !vipClientRequest) {
            return ResponseEntity.ok(buildInvalidRequestBody(AppConstants.RETURNED_JSON_BODY_FORBIDDEN, AppConstants.ERROR_BODY_EXCHANGE_RATES_DATE_RANGE_TOO_WIDE));
        }


        List<JsonConvertable> returnList = getExchangesFromMultipleDays(beginDate, endDate, requestedCurernciesList, baseCurrency);


        if (returnList.isEmpty()) {
            return ResponseEntity.ok(buildInvalidRequestBody(AppConstants.RETURNED_JSON_BODY_INTERNAL_SERVER_ERROR, AppConstants.ERROR_BODY_EXCHANGE_RATES_DATE_NOT_FOUND));
        }


        if (currencyValue != null && beginDate.equals(endDate) && requestedCurernciesList.size() == 1) {
            if (StringIntConverter.isStringValidDouble(currencyValue)) {
                CurrencyConversionReturnedObject currencyConversionReturnedObject = buildCurrencyConversionBody(baseCurrency, currencyValue, beginDate, requestedCurernciesList, returnList);
                return ResponseEntity.ok(currencyConversionReturnedObject.getBody());
            }
        }


        if (!vipClientRequest) {
            user.setCurrentRequestsCount(user.getCurrentRequestsCount() + 1);
            userRepository.save(user);
        }

        ExchangesList exchangesList = new ExchangesList();
        exchangesList.setExchangeList(returnList);

        return ResponseEntity.ok(exchangesList);
    }

    /**
     * Returns exchange rates from multiple days.
     */
    private List<JsonConvertable> getExchangesFromMultipleDays(LocalDate beginDate, LocalDate endDate, List<String> currency, String baseCurrency) {


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
     * Returns exchange rates from a single day.
     */
    private JsonConvertable getExchangesFromSingleDay(LocalDate inputDate, List<String> currency, String baseCurrency) {


        SingleDayExchangeRatesJson exchangeObject = new SingleDayExchangeRatesJson();

        if (!exchangeRepository.existsByExchangeDate(inputDate)) {
            exchangeObject.setSuccess(false);
            return exchangeObject;
        }

        try {
            List<Exchange> exchangesList;

            if (currency.get(0).equals(AppConstants.ALL_CURRENCIES)) {
                exchangesList = exchangeRepository.findAllByExchangeDateOrderByCurrencyDesc(inputDate);
            } else {
                exchangesList = exchangeRepository.findAllByExchangeDateAndCurrenciesOrderByExchangeDate(inputDate, currency);
            }


            if (baseCurrency.equals(AppConstants.EMPTY_STRING)) {
                exchangeObject.setBase(AppConstants.DEFAULT_BASE_CURRENCY);
            } else if (!CurrencyTypesValidator.isThisCurrencyAvailable(baseCurrency)) {
                return buildInvalidRequestBody(AppConstants.RETURNED_JSON_BODY_NOT_FOUND, AppConstants.ERROR_BODY_INVALID_BASE_CURRENCY);
            } else {
                exchangeObject.setBase(baseCurrency);
            }


            Map<String, Double> rates = new HashMap<>();
            if (exchangeObject.getBase().equals(AppConstants.DEFAULT_BASE_CURRENCY)) {
                for (Exchange entry : exchangesList) {
                    rates.put(entry.getCurrency().getIsoName(), entry.getValue());
                }
            } else {
                if (exchangeRepository.existsByExchangeDateAndCurrency_IsoName(inputDate, baseCurrency)) {
                    double newRatio = calculateNewRatio(inputDate, baseCurrency);
                    for (Exchange entry : exchangesList) {
                        rates.put(entry.getCurrency().getIsoName(), DecimalPlacesFixer.modifyNumberOfDecimalPlaces(entry.getValue() * newRatio, AppConstants.DECIMAL_PLACES_CURRENCY_CONVERSION));
                    }
                } else {
                    return buildInvalidRequestBody(AppConstants.RETURNED_JSON_BODY_NOT_FOUND, AppConstants.ERROR_BODY_EXCHANGE_RATES_DATE_CURRENCY_NOT_FOUND);
                }

            }

            exchangeObject.setDate(inputDate);
            exchangeObject.setRates(rates);
            exchangeObject.setSuccess(true);
        } catch (Exception e) {
            logger.error("Could not acquire latest exchange data: Exception: " + e);
        }
        return exchangeObject;
    }


    /**
     * Returns exchange rate based on non-default currency
     */

    private double calculateNewRatio(LocalDate date, String currency) {
        Exchange newBaseCurrency = exchangeRepository.findByExchangeDateAndCurrency_IsoName(date, currency);
        return DecimalPlacesFixer.modifyNumberOfDecimalPlaces(1 / newBaseCurrency.getValue(), AppConstants.DECIMAL_PLACES_CURRENCY_CONVERSION);
    }


    /**
     * Builds invalid request object
     */

    private InvalidRequestBody buildInvalidRequestBody(int httpErrorCode, String message) {
        InvalidRequestBody invalidRequestBody = new InvalidRequestBody();
        invalidRequestBody.setStatus(httpErrorCode);
        invalidRequestBody.setMessage(message);
        invalidRequestBody.setSuccess(false);

        logger.info(message);
        return invalidRequestBody;
    }


}
