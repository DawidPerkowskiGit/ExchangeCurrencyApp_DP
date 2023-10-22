package dpapps.exchangecurrencyapp.shedules;

import dpapps.exchangecurrencyapp.configuration.AppConstants;
import dpapps.exchangecurrencyapp.exchange.model.Exchange;
import dpapps.exchangecurrencyapp.exchange.model.User;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.UserRepository;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.ExternalJsonToDataParser;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.model.ExternalDataModel;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.DataFetcher;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.ExternalObjectToDatabaseCompatibleDataConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Performs scheduled automatic currency import and keeping the both apps running.
 */
public class ScheduledJobs {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Performs newest exchanges data import
     */
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

    /**
     * Calls apps URL to keep it running
     */
    public void performUrlRequestToKeepTheAppRunning() {
        LocalDateTime localDateTime = LocalDateTime.now();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://exchangecurrencyapp-dp-render.onrender.com/currencies")).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            request = HttpRequest.newBuilder().uri(URI.create(AppConstants.FRONTEND_APP_URL)).build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            logger.error("Failed to perform scheduled task. Exception: " + e);
        }
        logger.info("Successfully kept the app from un-allocating resources. Time: " + localDateTime.toString());
    }

    /**
     * Resets daily api usage count
     */
    public void resetDailyApiUsageCount(UserRepository userRepository) {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            user.setCurrentRequestsCount(0);
            userRepository.save(user);
        }
        logger.info("Successfully performed reset number of Api uses for every user. Time: " + LocalDateTime.now().toString());
        LocalDateTime.now();
    }
}
