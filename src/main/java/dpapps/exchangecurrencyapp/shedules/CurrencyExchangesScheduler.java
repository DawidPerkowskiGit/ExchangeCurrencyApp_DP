package dpapps.exchangecurrencyapp.shedules;

import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.jsonparser.ExchangeCurrencyPojo;
import dpapps.exchangecurrencyapp.jsonparser.ExchangeRatesJsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CurrencyExchangesScheduler {

    private final ExchangeRepository exchangeRepository;

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyExchangesScheduler(ExchangeRepository exchangeRepository, CurrencyRepository currencyRepository) {
        this.exchangeRepository = exchangeRepository;
        this.currencyRepository = currencyRepository;
    }

    @Scheduled(fixedRate = 14400000, initialDelay = 5000)
    public String performCurrencyExchangeImport() {
        System.out.println("Starting automatic currency exchange import");
        ExchangeRatesJsonParser exchangeRatesJsonParser = new ExchangeRatesJsonParser(this.exchangeRepository, this.currencyRepository);
        ExchangeCurrencyPojo exchangeCurrencyPojo = exchangeRatesJsonParser.parseJsonFromApi();
        List<Exchange> exchangeList = exchangeRatesJsonParser.convertExchangeCurrencyPojoToExchangeList(exchangeCurrencyPojo);
        exchangeRatesJsonParser.saveExchangeListToExchangeRepo(exchangeList);

        return "Success";
    }
}
