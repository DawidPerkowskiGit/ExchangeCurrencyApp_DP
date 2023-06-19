package dpapps.exchangecurrencyapp.jsonparser;

import dpapps.exchangecurrencyapp.exchange.entities.Currency;
import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for extracting data from ResponseBodyPojo object and inserting it to the database.
 */

@Service
public class ResponsePojoDatabaseInsert {

    private final ExchangeRepository exchangeRepository;

    private final CurrencyRepository currencyRepository;

    @Autowired
    public ResponsePojoDatabaseInsert(ExchangeRepository exchangeRepository, CurrencyRepository currencyRepository) {
        this.exchangeRepository = exchangeRepository;
        this.currencyRepository = currencyRepository;
    }

    public List<Exchange> convertPojoToExchangeList(ResponseBodyPojo responseBodyPojo) {
        List<Exchange> exchanges = new ArrayList<>();
        try {
            if (responseBodyPojo.getDate()==null) {
                System.out.println("Date is null");
                return exchanges;
            }
        }
        catch (Exception e) {
            System.out.println("Could not extract date from responseBodyPojo. Exception " + e);
        }


        try {
            if (exchangeRepository.existsByExchangeDate(responseBodyPojo.getDate())) {
                System.out.println("Exchange rates from " + responseBodyPojo.getDate().toString() + " are already in database");
                return exchanges;
            }
        }
        catch (Exception e) {
            System.out.println("Could not find date in Exchanges database");
        }


        try {
            for (AvailableCurrencyTypes currencyType: AvailableCurrencyTypes.values()
            ) {
                if (responseBodyPojo.getRates().containsKey(currencyType.toString())) {
                    Exchange exchange = new Exchange();
                    exchange.setCurrency(currencyRepository.findCurrencyByIsoName(currencyType.toString()));
                    exchange.setExchangeDate(responseBodyPojo.getDate());
                    exchange.setValue(responseBodyPojo.getRates().get(currencyType.toString()));
                    exchanges.add(exchange);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Could not create exchanges list. Exception "+ e);
        }
        System.out.println(exchanges);
        return exchanges;
    }

    public void insertExchangesToDatabase(List<Exchange> exchanges) {
        try {
            for (Exchange exchange: exchanges
            ) {
                exchangeRepository.save(exchange);
            }
        }
        catch (Exception e) {
            System.out.printf("Could not insert Exchanges to database");
        }
    }

}
