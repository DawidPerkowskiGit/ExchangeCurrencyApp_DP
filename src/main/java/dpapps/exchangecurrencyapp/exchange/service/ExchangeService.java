package dpapps.exchangecurrencyapp.exchange.service;

import dpapps.exchangecurrencyapp.jsonparser.entity.JsonConvertable;
import dpapps.exchangecurrencyapp.jsonparser.entity.SingleDayExchangeRatesJson;

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
}
