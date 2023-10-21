package dpapps.exchangecurrencyapp.shedules;

import dpapps.exchangecurrencyapp.exchange.model.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.ExternalJsonToDataParser;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.ExternalDataModel;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.DataFetcher;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.ExternalObjectToDatabaseCompatibleDataConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Service performs URL requestapi to keep this and frontend apps running
 */
public class ScheduleJobs {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public String performDailyDatabaseImport(String url, CurrencyRepository currencyRepository, ExchangeRepository exchangeRepository) {


        try {
            DataFetcher dataFetcher = new DataFetcher();
            String apiResponseBody = dataFetcher.fetchApiResponse(url);
            if (apiResponseBody.equals("")) {
                logger.info("Failed to import request api body");
                return "Failed to import request api body";
            }

            ExternalJsonToDataParser externalJsonToDataParser = new ExternalJsonToDataParser();
            Optional<ExternalDataModel> responseBodyPojo = externalJsonToDataParser.jsonDeserialization(apiResponseBody);
            if (responseBodyPojo.get().doAllNullableFieldsContainData() == false) {
                logger.info("Returned request api body is null");
                return "Returned request api body is null";
            }
            if (responseBodyPojo.get().getSuccess() == false) {
                logger.info("Could not get request api body");
                return "Could not get requestapi body";
            }

            ExternalObjectToDatabaseCompatibleDataConverter externalObjectToDatabaseCompatibleDataConverter = new ExternalObjectToDatabaseCompatibleDataConverter(exchangeRepository, currencyRepository);
            List<Exchange> exchanges = externalObjectToDatabaseCompatibleDataConverter.convertExternalToLocalData(responseBodyPojo.get());
            if (exchanges.isEmpty()) {
                logger.info("Exchange list is empty");
                return "Exchange list is empty";
            }

            externalObjectToDatabaseCompatibleDataConverter.saveExternalExchangeDataInDatabase(exchanges);
        } catch (Exception e) {
            logger.info("Could not perform scheduled exchange rates import");
        }
        logger.info("Exchange rates imported successfully");
        return "Exchange rates imported successfully";
    }
}
