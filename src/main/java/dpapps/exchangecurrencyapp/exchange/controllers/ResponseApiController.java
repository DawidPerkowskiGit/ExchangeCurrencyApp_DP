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
import dpapps.exchangecurrencyapp.jsonparser.responsepojo.CurrenciesListPojo;
import dpapps.exchangecurrencyapp.jsonparser.responsepojo.CurrencyExchangesFromSingleDayPojo;
import dpapps.exchangecurrencyapp.jsonparser.responsepojo.ResponsePojo;
import dpapps.exchangecurrencyapp.security.UserService;
import dpapps.exchangecurrencyapp.shedules.ScheduleJobs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ResponseApiController {
    CurrencyRepository currencyRepository;

    ExchangeRepository exchangeRepository;

    LocationRepository locationRepository;

    LocationCurrencyPairRepository locationCurrencyPairRepository;
    private final ApiKeyRepository apiKeyRepository;

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public ResponseApiController(CurrencyRepository currencyRepository,
                                 ExchangeRepository exchangeRepository,
                                 LocationRepository locationRepository,
                                 LocationCurrencyPairRepository locationCurrencyPairRepository,
                                 ApiKeyRepository apiKeyRepository,
                                 UserService userService,
                                 UserRepository userRepository) {
        this.currencyRepository = currencyRepository;
        this.exchangeRepository = exchangeRepository;
        this.locationRepository = locationRepository;
        this.locationCurrencyPairRepository = locationCurrencyPairRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/currencies")
    public String getCurrencies() {
        CurrenciesListPojo currencyListPojo = new CurrenciesListPojo();
        Iterable<Currency> currencyList = currencyRepository.findAll();
        currencyListPojo.convertCurrencyListToJsonCurrency(currencyList);

        return buildJsonFromPojo(currencyListPojo);
//        return currencyRepository.findAll().toString();
    }


    @GetMapping("/forceRequestUrl")
    public String forceRequestUrl(@RequestParam(required = false) String apiKey,
                                  @RequestParam(required = false) String requestUrl) {

        String apiKeyParsingResult = checkApiKey(apiKey);
        if ( ! apiKeyParsingResult.equals(AppVariables.VALID_API_KEY_MESSAGE)) {
            return apiKeyParsingResult;
        }

        if (requestUrl == null) {
            return "request Url is null";
        }

        ScheduleJobs scheduleJobs = new ScheduleJobs();

        return scheduleJobs.performDailyDatabaseImport(requestUrl, this.currencyRepository, this.exchangeRepository);


    }

    private String checkApiKey(String apiKey) {
        if (apiKey == null) {
            return "You didnt provide api key";
        }

        if (apiKeyRepository.existsByValue(apiKey) == false) {
            return "Api key is invalid";
        }

        return AppVariables.VALID_API_KEY_MESSAGE;
    }

    @GetMapping("/exchange")
    public String getExchangeRates(@RequestParam(required = false) String currency,
                                   @RequestParam(required = false) String startDate,
                                   @RequestParam(required = false) String finishDate,
                                   @RequestParam(required = false) String baseCurrency,
                                   @RequestParam(required = false) String apiKey) {

        System.out.println("entered exchange");

        String apiKeyParsingResult = checkApiKey(apiKey);
        if ( ! apiKeyParsingResult.equals(AppVariables.VALID_API_KEY_MESSAGE)) {
            return apiKeyParsingResult;
        }

        ApiKey apiKeyObject = apiKeyRepository.findByValue(apiKey);

/*        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());*/
        User user = apiKeyObject.getUser();
        ApiKeyManager apiKeyManager = new ApiKeyManager(user, apiKeyRepository);
        if (apiKeyManager.canUseTheApiKey(apiKeyObject) == false) {
            return "Cannot perform API request";
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
        } else {
            pojoList = getExchangesFromMultipleDays(beginDate, endDate, currency, baseCurrency);
            returnedJson = buildJsonFromPojo(pojoList);
        }
//        int numberOfUsages = userRepository.howManyUsages(user.getId()) + 1;
//        userRepository.increaseNumberOfUsages(user.getId(), numberOfUsages);
        user.setCurrentRequestsCount(user.getCurrentRequestsCount() + 1);
        userRepository.save(user);
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

    public double calculateNewRatio(LocalDate date, String currency) {
        Exchange newBaseCurrency = exchangeRepository.findByExchangeDateAndCurrency_IsoName(date, currency);
        return 1 / newBaseCurrency.getValue();
    }


    public String buildJsonFromPojo(CurrencyExchangesFromSingleDayPojo pojo) {

        ObjectMapper objectMapper = new ObjectMapper();
        String exchangesToJson = "";
        try {
            exchangesToJson = objectMapper.writeValueAsString(pojo);
        } catch (Exception e) {
            System.out.println("Could not map object to JSON. Exception: " + e);
        }

        return exchangesToJson;
    }

    public String buildJsonFromPojo(List<CurrencyExchangesFromSingleDayPojo> pojoList) {

        ObjectMapper objectMapper = new ObjectMapper();
        String exchangesToJson = "";
        try {
            exchangesToJson = objectMapper.writeValueAsString(pojoList);
        } catch (Exception e) {
            System.out.println("Could not map object to JSON. Exception: " + e);
        }

        return exchangesToJson;
    }

    public String buildJsonFromPojo(CurrenciesListPojo pojoList) {

        ObjectMapper objectMapper = new ObjectMapper();
        String exchangesToJson = "";
        try {
            exchangesToJson = objectMapper.writeValueAsString(pojoList);
        } catch (Exception e) {
            System.out.println("Could not map object to JSON. Exception: " + e);
        }

        return exchangesToJson;
    }
    public LocalDate returnExchangeDateThatExistsInDb(LocalDate date) {

        while (DateRange.isDateInValidRange(date)) {
            if (exchangeRepository.existsByExchangeDate(date) == false) {
                date = date.minusDays(1);
            } else {
                return date;
            }
        }
        return AppVariables.EXCHANGE_DATE_OLDEST;
    }

}
