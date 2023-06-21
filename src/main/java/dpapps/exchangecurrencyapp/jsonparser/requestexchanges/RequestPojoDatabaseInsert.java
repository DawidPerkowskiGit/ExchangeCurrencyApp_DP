package dpapps.exchangecurrencyapp.jsonparser.requestexchanges;

import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.exchange.tools.AvailableCurrencyTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This module is responsible for extracting data from ResponseBodyPojo object and inserting it to the database.
 */

@Service
public class RequestPojoDatabaseInsert {

    private final ExchangeRepository exchangeRepository;

    private final CurrencyRepository currencyRepository;

    @Autowired
    public RequestPojoDatabaseInsert(ExchangeRepository exchangeRepository, CurrencyRepository currencyRepository) {
        this.exchangeRepository = exchangeRepository;
        this.currencyRepository = currencyRepository;
    }

    public List<Exchange> convertPojoToExchangeList(RequestBodyObject requestBodyObject) {
        List<Exchange> exchanges = new ArrayList<>();
        try {
            if (requestBodyObject.doAllNullableFieldsContainData() == false) {
                System.out.println("Exchange date does not contain information");
                return exchanges;
            }
        }
        catch (Exception e) {
            System.out.println("Could not extract date from responseBodyPojo. Exception " + e);
        }


        try {
            if (exchangeRepository.existsByExchangeDate(requestBodyObject.getDate())) {
                System.out.println("Exchange rates from " + requestBodyObject.getDate().toString() + " are already in database");
                return exchanges;
            }
        }
        catch (Exception e) {
            System.out.println("Could not find date in Exchanges database");
        }


        try {
            for (AvailableCurrencyTypes currencyType: AvailableCurrencyTypes.values()
            ) {
                if (requestBodyObject.getRates().containsKey(currencyType.toString())) {
                    Exchange exchange = new Exchange();
                    exchange.setCurrency(currencyRepository.findCurrencyByIsoName(currencyType.toString()));
                    exchange.setExchangeDate(requestBodyObject.getDate());
                    exchange.setValue(requestBodyObject.getRates().get(currencyType.toString()));
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
