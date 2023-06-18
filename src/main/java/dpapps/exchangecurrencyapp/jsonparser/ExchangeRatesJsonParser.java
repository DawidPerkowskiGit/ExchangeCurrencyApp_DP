package dpapps.exchangecurrencyapp.jsonparser;

import com.fasterxml.jackson.databind.ObjectMapper;
import dpapps.exchangecurrencyapp.exchange.entities.Currency;
import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangeRatesJsonParser {

    private final boolean DEBUG = true;

    private final ExchangeRepository exchangeRepository;

    private final CurrencyRepository currencyRepository;

    @Autowired
    public ExchangeRatesJsonParser(ExchangeRepository exchangeRepository, CurrencyRepository currencyRepository) {
        this.exchangeRepository = exchangeRepository;
        this.currencyRepository = currencyRepository;
    }

    public ExchangeCurrencyPojo parseJsonFromFile(String filePath) {

        ExchangeCurrencyPojo exchangeCurrencyPojo = new ExchangeCurrencyPojo();

        try {
            String jsonInString = readFileAsString(filePath);
            ObjectMapper mapper = new ObjectMapper();
            exchangeCurrencyPojo = mapper.readValue(jsonInString, ExchangeCurrencyPojo.class);
            if (DEBUG) {
                String pojoToString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(exchangeCurrencyPojo);
                System.out.println(pojoToString);
            }
        }
        catch (Exception e) {
            System.out.println("Could not parse JSON body. Exception: " + e);
        }
        return exchangeCurrencyPojo;
    }

    public ExchangeCurrencyPojo parseJsonFromApi() {

        ExchangeCurrencyPojo exchangeCurrencyPojo = new ExchangeCurrencyPojo();

        try {
            String jsonInString = new DailyExchangeImporter().retireveApiResponse();
            ObjectMapper mapper = new ObjectMapper();
            exchangeCurrencyPojo = mapper.readValue(jsonInString, ExchangeCurrencyPojo.class);
            if (DEBUG) {
                String pojoToString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(exchangeCurrencyPojo);
                System.out.println(pojoToString);
            }
        }
        catch (Exception e) {
            System.out.println("Could not parse JSON body. Exception: " + e);
        }

        return exchangeCurrencyPojo;
    }

    public List<Exchange> convertExchangeCurrencyPojoToExchangeList(ExchangeCurrencyPojo exchangeCurrencyPojo) {
        List<Exchange> exchanges = new ArrayList<>();

        if (exchangeRepository.existsByExchangeDate(exchangeCurrencyPojo.getDate())) {
            return exchanges;
        }
        for (ExchangeCurrencyTypes currencyType: ExchangeCurrencyTypes.values()
             ) {
            if (exchangeCurrencyPojo.getRates().containsKey(currencyType.toString())) {
                Exchange exchange = new Exchange();
                exchange.setCurrency(currencyRepository.findCurrencyByIsoName(currencyType.toString()));
                exchange.setExchangeDate(exchangeCurrencyPojo.getDate());
                exchange.setValue(exchangeCurrencyPojo.getRates().get(currencyType.toString()));
                exchanges.add(exchange);
            }
        }
        System.out.println(exchanges);
        return exchanges;
    }

    public void saveExchangeListToExchangeRepo(List<Exchange> exchanges) {
        for (Exchange exchange: exchanges
             ) {
            exchangeRepository.save(exchange);
        }
    }

    public String readFileAsString(String file)throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(file)));
    }


    public void addNewExchange(String currencyName, LocalDate exchangeDate, String value) {
        Currency currency = currencyRepository.findCurrencyByIsoName(currencyName);
        Exchange exchange = new Exchange();
        exchange.setCurrency(currency);
        exchange.setValue(Double.parseDouble(value));

        exchange.setExchangeDate(exchangeDate);
        exchangeRepository.save(exchange);
        if (DEBUG) {
            System.out.println(exchange);
        }
    }

}
