package dpapps.exchangecurrencyapp.exchange.controllers;

import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.jsonparser.DailyExchangeImporter;
import dpapps.exchangecurrencyapp.jsonparser.ExchangeCurrencyPojo;
import dpapps.exchangecurrencyapp.jsonparser.ExchangeRatesJsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExchangeController {
    private final ExchangeRepository exchangeRepository;

    private final CurrencyRepository currencyRepository;


    @Autowired
    public ExchangeController(ExchangeRepository exchangeRepository, CurrencyRepository currencyRepository) {
        this.exchangeRepository = exchangeRepository;
        this.currencyRepository = currencyRepository;
    }

    @GetMapping("/currencies")
    public String getCurrencies() {
        return currencyRepository.findAll().toString();
    }

/*    @GetMapping("/parse")
    public void parseFile() {
        ExchangeRatesJsonParser exchangeRatesJsonParser = new ExchangeRatesJsonParser(this.exchangeRepository, this.currencyRepository);
        ExchangeCurrencyPojo exchangeCurrencyPojo = new ExchangeCurrencyPojo();
        String filePath = "src/main/resources/exchangeRatesApiResponse.json";
        exchangeCurrencyPojo = exchangeRatesJsonParser.parseJsonFromFile(filePath);
        //exchangeRatesJsonParser.parseJson();
        List<Exchange> exchangeList = exchangeRatesJsonParser.convertExchangeCurrencyPojoToExchangeList(exchangeCurrencyPojo);
        exchangeRatesJsonParser.saveExchangeListToExchangeRepo(exchangeList);
    }

    @GetMapping("/api")
    public void apiresposne() {
        DailyExchangeImporter dailyExchangeImporter = new DailyExchangeImporter();
        dailyExchangeImporter.retireveApiResponse();
    }*/

    //public HttpRequest

}
