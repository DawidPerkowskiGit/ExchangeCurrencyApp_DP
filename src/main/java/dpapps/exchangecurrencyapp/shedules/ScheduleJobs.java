package dpapps.exchangecurrencyapp.shedules;

import dpapps.exchangecurrencyapp.exchange.model.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.JsonToDataParser;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.RequestDataModel;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.DataFetcher;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.DataToDatabaseInserter;
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

            JsonToDataParser jsonToDataParser = new JsonToDataParser();
            Optional<RequestDataModel> responseBodyPojo = jsonToDataParser.jsonDeserialization(apiResponseBody);
            if (responseBodyPojo.get().doAllNullableFieldsContainData() == false) {
                logger.info("Returned request api body is null");
                return "Returned request api body is null";
            }
            if (responseBodyPojo.get().getSuccess() == false) {
                logger.info("Could not get request api body");
                return "Could not get requestapi body";
            }

            DataToDatabaseInserter dataToDatabaseInserter = new DataToDatabaseInserter(exchangeRepository, currencyRepository);
            List<Exchange> exchanges = dataToDatabaseInserter.convertPojoToExchangeList(responseBodyPojo.get());
            if (exchanges.isEmpty()) {
                logger.info("Exchange list is empty");
                return "Exchange list is empty";
            }

            dataToDatabaseInserter.insertExchangesToDatabase(exchanges);
        } catch (Exception e) {
            logger.info("Could not perform scheduled exchange rates import");
        }
        logger.info("Exchange rates imported successfully");
        return "Exchange rates imported successfully";
    }
}
