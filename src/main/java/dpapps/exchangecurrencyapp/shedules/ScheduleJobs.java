package dpapps.exchangecurrencyapp.shedules;

import dpapps.exchangecurrencyapp.exchange.model.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.JsonToDataParser;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.RequestDataModel;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.DataFetcher;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.DataToDatabaseInserter;

import java.util.List;
import java.util.Optional;

/**
 * Service performs URL requestapi to keep this and frontend apps running
 */
public class ScheduleJobs {
    public String performDailyDatabaseImport(String url, CurrencyRepository currencyRepository, ExchangeRepository exchangeRepository) {


        try {
            DataFetcher dataFetcher = new DataFetcher();
            String apiResponseBody = dataFetcher.retrieveApiResponse(url);
            if (apiResponseBody.equals("")) {
                System.out.println("Failed to import requestapi body");
                return "Failed to import requestapi body";
            }

            JsonToDataParser jsonToDataParser = new JsonToDataParser();
            Optional<RequestDataModel> responseBodyPojo = jsonToDataParser.jsonDeserialization(apiResponseBody);
            if (responseBodyPojo.get().doAllNullableFieldsContainData() == false) {
                System.out.println("Returned requestapi body is null");
                return "Returned requestapi body is null";
            }
            if (responseBodyPojo.get().getSuccess() == false) {
                System.out.println("Could not get requestapi body");
                return "Could not get requestapi body";
            }

            DataToDatabaseInserter dataToDatabaseInserter = new DataToDatabaseInserter(exchangeRepository, currencyRepository);
            List<Exchange> exchanges = dataToDatabaseInserter.convertPojoToExchangeList(responseBodyPojo.get());
            if (exchanges.isEmpty()) {
                System.out.println("Exchange list is empty");
                return "Exchange list is empty";
            }

            dataToDatabaseInserter.insertExchangesToDatabase(exchanges);
        } catch (Exception e) {
            System.out.println("Could not perform scheduled exchange rates import");
        }

        System.out.println("Exchange rates imported successfully");

        return "Exchange rates imported successfully";
    }
}
