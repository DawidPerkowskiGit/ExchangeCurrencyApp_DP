package dpapps.exchangecurrencyapp.shedules;

import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.jsonparser.ResponseBodyPojo;
import dpapps.exchangecurrencyapp.jsonparser.ResponseBodyJsonParser;
import dpapps.exchangecurrencyapp.jsonparser.ResponseBodyRetriever;
import dpapps.exchangecurrencyapp.jsonparser.ResponsePojoDatabaseInsert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This component is responsible for performing extracting exchange rates from API endpoint.
 * Fetched request is inserted into database.
 */

@Component
public class CurrencyExchangesScheduler {

    private final ExchangeRepository exchangeRepository;

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyExchangesScheduler(ExchangeRepository exchangeRepository, CurrencyRepository currencyRepository) {
        this.exchangeRepository = exchangeRepository;
        this.currencyRepository = currencyRepository;
    }

    @Scheduled(fixedRate = 14400000, initialDelay = 3600000)
    public String performCurrencyExchangeImport() {
        System.out.println("Starting automatic currency exchange import");

        try {
            ResponseBodyRetriever responseBodyRetriever = new ResponseBodyRetriever();
            String apiResponseBody = responseBodyRetriever.retrieveApiResponse();
            if (apiResponseBody.equals("")) {
                System.out.println("Failed to import response body");
                return "Failed to import response body";
            }

            ResponseBodyJsonParser responseBodyJsonParser = new ResponseBodyJsonParser();
            ResponseBodyPojo responseBodyPojo = responseBodyJsonParser.jsonDeserialization(apiResponseBody);
            if (responseBodyPojo==null) {
                System.out.println("Returned response body is null");
                return "Returned response body is null";
            }
            if (responseBodyPojo.getSuccess()==false) {
                System.out.println("Could not get response body");
                return "Could not get response body";
            }

            ResponsePojoDatabaseInsert responsePojoDatabaseInsert = new ResponsePojoDatabaseInsert(this.exchangeRepository, this.currencyRepository);
            List<Exchange> exchanges = responsePojoDatabaseInsert.convertPojoToExchangeList(responseBodyPojo);
            if (exchanges.isEmpty()) {
                System.out.println("Exchange list is empty");
                return "Exchange list is empty";
            }

            responsePojoDatabaseInsert.insertExchangesToDatabase(exchanges);
        }
        catch (Exception e) {
            System.out.println("Could not perform scheduled exchange rates import");
        }

        System.out.println("Exchange rates imported successfully");

        return "Exchange rates imported successfully";
    }

    /**
     * Every 10 minutes send simple request
     */
    @Scheduled(fixedRate = 600000)
    public String keepTheAppRunning(){
        LocalDateTime localDateTime = LocalDateTime.now();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://exchangecurrencyapp-dp-render.onrender.com/currencies"))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception e) {
            System.out.println("Failed to perform scheduled task. Exception: "+ e);
        }
        System.out.println("Successfully kept the app from un-allocating resources. Time: " + localDateTime.toString());
        return "The app is running";
    }
}
