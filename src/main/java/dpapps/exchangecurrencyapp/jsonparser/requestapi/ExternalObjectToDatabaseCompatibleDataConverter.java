package dpapps.exchangecurrencyapp.jsonparser.requestapi;

import dpapps.exchangecurrencyapp.exchange.model.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.exchange.helpers.CurrencyTypes;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.model.ExternalDataModel;
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
public class ExternalObjectToDatabaseCompatibleDataConverter {

    private final ExchangeRepository exchangeRepository;

    private final CurrencyRepository currencyRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ExternalObjectToDatabaseCompatibleDataConverter(ExchangeRepository exchangeRepository, CurrencyRepository currencyRepository) {
        this.exchangeRepository = exchangeRepository;
        this.currencyRepository = currencyRepository;
    }

    /**
     * Converts external currency exchange service data to object compatible with current database, ready to be inserted.
     */
    public List<Exchange> convertExternalToLocalData(ExternalDataModel externalDataModel) {
        List<Exchange> exchanges = new ArrayList<>();
        try {
            if (externalDataModel.doAllNullableFieldsContainData() == false) {
                logger.info("Exchange date does not contain information");
                return exchanges;
            }
        } catch (Exception e) {
            logger.error("Could not extract date from responseBodyPojo. Exception " + e);
        }


        try {
            if (exchangeRepository.existsByExchangeDate(externalDataModel.getDate())) {
                logger.info("Exchange rates from " + externalDataModel.getDate().toString() + " are already in database");
                return exchanges;
            }
        } catch (Exception e) {
            logger.warn("Could not find date in Exchanges database");
        }


        try {
            for (CurrencyTypes currencyType : CurrencyTypes.values()) {
                if (externalDataModel.getRates().containsKey(currencyType.toString())) {
                    Exchange exchange = new Exchange();
                    exchange.setCurrency(currencyRepository.findCurrencyByIsoName(currencyType.toString()));
                    exchange.setExchangeDate(externalDataModel.getDate());
                    exchange.setValue(externalDataModel.getRates().get(currencyType.toString()));
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
     * Saves converted Exchange data in the database.
     */
    public void saveExternalExchangeDataInDatabase(List<Exchange> exchanges) {
        try {
            for (Exchange exchange : exchanges) {
                exchangeRepository.save(exchange);
            }
        } catch (Exception e) {
            logger.error("Could not insert Exchanges to database");
        }
    }

}
