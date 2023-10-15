package dpapps.exchangecurrencyapp.jsonparser.requestapi;

import dpapps.exchangecurrencyapp.exchange.model.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.exchange.helpers.AvailableCurrencyTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
                logger.info("Exchange date does not contain information");
                return exchanges;
            }
        } catch (Exception e) {
            logger.error("Could not extract date from responseBodyPojo. Exception " + e);
        }


        try {
            if (exchangeRepository.existsByExchangeDate(requestDataModel.getDate())) {
                logger.info("Exchange rates from " + requestDataModel.getDate().toString() + " are already in database");
                return exchanges;
            }
        } catch (Exception e) {
            logger.warn("Could not find date in Exchanges database");
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
            logger.warn("Could not create exchanges list. Exception " + e);
        }
        logger.info("Exchanges inserted to the database : " + exchanges);
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
            logger.error("Could not insert Exchanges to database");
        }
    }

}
