package dpapps.exchangecurrencyapp.shedules;

import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.jsonparser.requestexchanges.RequestBodyJsonParser;
import dpapps.exchangecurrencyapp.jsonparser.requestexchanges.RequestBodyObject;
import dpapps.exchangecurrencyapp.jsonparser.requestexchanges.RequestBodyRetriever;
import dpapps.exchangecurrencyapp.jsonparser.requestexchanges.RequestPojoDatabaseInsert;

import java.util.List;
import java.util.Optional;

public class ScheduleJobs {
    public String performDailyDatabaseImport(String url, CurrencyRepository currencyRepository, ExchangeRepository exchangeRepository) {


        try {
            RequestBodyRetriever requestBodyRetriever = new RequestBodyRetriever();
            String apiResponseBody = requestBodyRetriever.retrieveApiResponse(url);
            if (apiResponseBody.equals("")) {
                System.out.println("Failed to import response body");
                return "Failed to import response body";
            }

            RequestBodyJsonParser requestBodyJsonParser = new RequestBodyJsonParser();
            Optional<RequestBodyObject> responseBodyPojo = requestBodyJsonParser.jsonDeserialization(apiResponseBody);
            if (responseBodyPojo.get().doAllNullableFieldsContainData() == false) {
                System.out.println("Returned response body is null");
                return "Returned response body is null";
            }
            if (responseBodyPojo.get().getSuccess() == false) {
                System.out.println("Could not get response body");
                return "Could not get response body";
            }

            RequestPojoDatabaseInsert responsePojoDatabaseInsert = new RequestPojoDatabaseInsert(exchangeRepository, currencyRepository);
            List<Exchange> exchanges = responsePojoDatabaseInsert.convertPojoToExchangeList(responseBodyPojo.get());
            if (exchanges.isEmpty()) {
                System.out.println("Exchange list is empty");
                return "Exchange list is empty";
            }

            responsePojoDatabaseInsert.insertExchangesToDatabase(exchanges);
        } catch (Exception e) {
            System.out.println("Could not perform scheduled exchange rates import");
        }

        System.out.println("Exchange rates imported successfully");

        return "Exchange rates imported successfully";
    }
}
