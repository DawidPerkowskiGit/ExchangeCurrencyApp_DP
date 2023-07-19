package dpapps.exchangecurrencyapp.shedules;

import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.entities.User;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.UserRepository;
import dpapps.exchangecurrencyapp.jsonparser.requestexchanges.RequestBodyObject;
import dpapps.exchangecurrencyapp.jsonparser.requestexchanges.RequestBodyJsonParser;
import dpapps.exchangecurrencyapp.jsonparser.requestexchanges.RequestBodyRetriever;
import dpapps.exchangecurrencyapp.jsonparser.requestexchanges.RequestPojoDatabaseInsert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * This component is responsible for performing extracting exchange rates from API endpoint.
 * Fetched request is inserted into database.
 */

@Component
public class CurrencyExchangesScheduler {

    private final ExchangeRepository exchangeRepository;

    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;

    @Autowired
    public CurrencyExchangesScheduler(ExchangeRepository exchangeRepository, CurrencyRepository currencyRepository,
                                      UserRepository userRepository) {
        this.exchangeRepository = exchangeRepository;
        this.currencyRepository = currencyRepository;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRate = 14400000, initialDelay = 3600000)
    public String performCurrencyExchangeImport() {
        System.out.println("Starting automatic currency exchange import");

        try {
            RequestBodyRetriever requestBodyRetriever = new RequestBodyRetriever();
            String apiResponseBody = requestBodyRetriever.retrieveApiResponse();
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
            if (responseBodyPojo.get().getSuccess()==false) {
                System.out.println("Could not get response body");
                return "Could not get response body";
            }

            RequestPojoDatabaseInsert responsePojoDatabaseInsert = new RequestPojoDatabaseInsert(this.exchangeRepository, this.currencyRepository);
            List<Exchange> exchanges = responsePojoDatabaseInsert.convertPojoToExchangeList(responseBodyPojo.get());
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
/*    @Scheduled(fixedRate = 600000, initialDelay = 600000)
    public void keepTheAppRunning(){
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
    }*/

    @Scheduled(cron = "0 0 1 * * *", zone = "Europe/Paris")
    public void apiUsagesReset() {
        List<User> userList = userRepository.findAll();
        for (User user: userList
             ) {
            user.setCurrentRequestsCount(0);
            userRepository.save(user);
        }
        System.out.println("Successfully reseted number of Api uses for every user. Time: " + LocalDateTime.now().toString());
        LocalDateTime.now();
    }
}
