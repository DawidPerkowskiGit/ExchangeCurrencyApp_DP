package dpapps.exchangecurrencyapp.jsonparser.requestapi;

import dpapps.exchangecurrencyapp.exchange.model.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.exchange.tools.AvailableCurrencyTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This module is responsible for inserting retrieved data from exchangeratesapi.io and inserting it to the database.
 */

@Service
public class DataToDatabaseInserter {

    private final ExchangeRepository exchangeRepository;

    private final CurrencyRepository currencyRepository;

    @Autowired
    public DataToDatabaseInserter(ExchangeRepository exchangeRepository, CurrencyRepository currencyRepository) {
        this.exchangeRepository = exchangeRepository;
        this.currencyRepository = currencyRepository;
    }

    /**
     * Converts RequestDataModel[] to model Exchange list
     *
     * @param requestDataModel input Objects
     * @return model Exchange
     */
    public List<Exchange> convertPojoToExchangeList(RequestDataModel requestDataModel) {
        List<Exchange> exchanges = new ArrayList<>();
        try {
            if (requestDataModel.doAllNullableFieldsContainData() == false) {
                System.out.println("Exchange date does not contain information");
                return exchanges;
            }
        } catch (Exception e) {
            System.out.println("Could not extract date from responseBodyPojo. Exception " + e);
        }


        try {
            if (exchangeRepository.existsByExchangeDate(requestDataModel.getDate())) {
                System.out.println("Exchange rates from " + requestDataModel.getDate().toString() + " are already in database");
                return exchanges;
            }
        } catch (Exception e) {
            System.out.println("Could not find date in Exchanges database");
        }


        try {
            for (AvailableCurrencyTypes currencyType : AvailableCurrencyTypes.values()) {
                if (requestDataModel.getRates().containsKey(currencyType.toString())) {
                    Exchange exchange = new Exchange();
                    exchange.setCurrency(currencyRepository.findCurrencyByIsoName(currencyType.toString()));
                    exchange.setExchangeDate(requestDataModel.getDate());
                    exchange.setValue(requestDataModel.getRates().get(currencyType.toString()));
                    exchanges.add(exchange);
                }
            }
        } catch (Exception e) {
            System.out.println("Could not create exchanges list. Exception " + e);
        }
        System.out.println(exchanges);
        return exchanges;
    }

    /**
     * Saves model Exchange data to the database
     *
     * @param exchanges model Exchange data
     */
    public void insertExchangesToDatabase(List<Exchange> exchanges) {
        try {
            for (Exchange exchange : exchanges) {
                exchangeRepository.save(exchange);
            }
        } catch (Exception e) {
            System.out.printf("Could not insert Exchanges to database");
        }
    }

}
