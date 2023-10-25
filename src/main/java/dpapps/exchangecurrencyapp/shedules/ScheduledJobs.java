package dpapps.exchangecurrencyapp.shedules;

import dpapps.exchangecurrencyapp.configuration.AppConstants;
import dpapps.exchangecurrencyapp.exchange.model.Exchange;
import dpapps.exchangecurrencyapp.exchange.model.User;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.UserRepository;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.DataFetcher;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.ExternalJsonToDataParser;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.ExternalObjectToDatabaseCompatibleDataConverter;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.model.ExternalDataModel;
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
            if (apiResponseBody.equals(AppConstants.EMPTY_STRING)) {
                String info = "Failed to import request body";
                logger.info(info);
                return info;
            }

            ExternalJsonToDataParser externalJsonToDataParser = new ExternalJsonToDataParser();
            Optional<ExternalDataModel> responseBodyPojo = externalJsonToDataParser.jsonDeserialization(apiResponseBody);
            if (!responseBodyPojo.get().doAllNullableFieldsContainData()) {
                String info = "Returned request body is null";
                logger.info(info);
                return info;
            }
            if (!responseBodyPojo.get().getSuccess()) {
                String info = "Could not get request body";
                logger.info(info);
                return info;
            }

            ExternalObjectToDatabaseCompatibleDataConverter externalObjectToDatabaseCompatibleDataConverter = new ExternalObjectToDatabaseCompatibleDataConverter(exchangeRepository, currencyRepository);
            List<Exchange> exchanges = externalObjectToDatabaseCompatibleDataConverter.convertExternalToLocalData(responseBodyPojo.get());
            if (exchanges.isEmpty()) {
                String info = "Exchange list is empty";
                logger.info(info);
                return info;
            }

            externalObjectToDatabaseCompatibleDataConverter.saveExternalExchangeDataInDatabase(exchanges);
        } catch (Exception e) {
            logger.info("Could not perform scheduled exchange rates import");
        }
        String info = "Exchange rates imported successfully";
        logger.info(info);
        return info;
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
        logger.info("Successfully kept the app from un-allocating resources. Time: " + localDateTime);
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
        logger.info("Successfully performed reset number of Api uses for every user. Time: " + LocalDateTime.now());
        LocalDateTime.now();
    }
}
