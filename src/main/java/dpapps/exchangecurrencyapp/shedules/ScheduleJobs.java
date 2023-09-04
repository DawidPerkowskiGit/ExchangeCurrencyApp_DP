package dpapps.exchangecurrencyapp.shedules;

import dpapps.exchangecurrencyapp.exchange.model.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.jsonparser.responseforeignapi.ResponseBodyJsonParser;
import dpapps.exchangecurrencyapp.jsonparser.responseforeignapi.ResponseBodyObject;
import dpapps.exchangecurrencyapp.jsonparser.responseforeignapi.ResponseBodyFetcher;
import dpapps.exchangecurrencyapp.jsonparser.responseforeignapi.ResponsePojoDatabaseInsert;

import java.util.List;
import java.util.Optional;

/**
 * Service performs URL request to keep this and frontend apps running
 */
public class ScheduleJobs {
    public String performDailyDatabaseImport(String url, CurrencyRepository currencyRepository, ExchangeRepository exchangeRepository) {


        try {
            ResponseBodyFetcher responseBodyFetcher = new ResponseBodyFetcher();
            String apiResponseBody = responseBodyFetcher.retrieveApiResponse(url);
            if (apiResponseBody.equals("")) {
                System.out.println("Failed to import response body");
                return "Failed to import response body";
            }

            ResponseBodyJsonParser responseBodyJsonParser = new ResponseBodyJsonParser();
            Optional<ResponseBodyObject> responseBodyPojo = responseBodyJsonParser.jsonDeserialization(apiResponseBody);
            if (responseBodyPojo.get().doAllNullableFieldsContainData() == false) {
                System.out.println("Returned response body is null");
                return "Returned response body is null";
            }
            if (responseBodyPojo.get().getSuccess() == false) {
                System.out.println("Could not get response body");
                return "Could not get response body";
            }

            ResponsePojoDatabaseInsert responsePojoDatabaseInsert = new ResponsePojoDatabaseInsert(exchangeRepository, currencyRepository);
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
