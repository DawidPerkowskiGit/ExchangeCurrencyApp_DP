package dpapps.exchangecurrencyapp.exchange.service;

import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonConvertable;
import dpapps.exchangecurrencyapp.jsonparser.response.SingleDayExchangeRatesJson;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ExchangeService {

    public String getCurrencies(String date);
    public String getCurrenciesAndLocations();
    public String manualRequestUrl(String apiKey, String requestUrl);

    public String getExchanges(String currency, String startDate, String finishDate, String baseCurrency, String apiKey, Map<String, String> headers);

    public List<JsonConvertable> getExchangesFromMultipleDays(LocalDate beginDate, LocalDate endDate, String currency, String baseCurrency);

    public Optional<SingleDayExchangeRatesJson> getExchangesFromSingleDay(LocalDate inputDate, String currency, String baseCurrency);

    public double calculateNewRatio(LocalDate date, String currency);

    /**
     * Converts returned database rows to object ready for serialization
     *
     * @param databaseEntries entire retrieved from the database, structure [iso_name, full_name, location]
     * @return Object of structure [iso_name, full_name, List<location>] ready for serialization
     */
    public List<JsonConvertable> convertDbCurrencyNameLocationToObjectList(Iterable<String[]> databaseEntries);

    /**
     * Adds entry to List property of a HashMap
     *
     * @param value Value needed to be added to the List
     * @param list  List of entries
     * @return List with an additional value
     */
    public List<String> addValueReplaceMap(String value, List<String> list);
}
