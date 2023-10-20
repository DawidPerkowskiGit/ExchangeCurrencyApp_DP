package dpapps.exchangecurrencyapp.shedules;

import dpapps.exchangecurrencyapp.configuration.AppConstants;
import dpapps.exchangecurrencyapp.exchange.model.User;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Fetched requestapi is inserted into database.
 */

@Component
public class CurrencyExchangesScheduler {

    private final ExchangeRepository exchangeRepository;

    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CurrencyExchangesScheduler(ExchangeRepository exchangeRepository, CurrencyRepository currencyRepository, UserRepository userRepository) {
        this.exchangeRepository = exchangeRepository;
        this.currencyRepository = currencyRepository;
        this.userRepository = userRepository;
    }

    /**
     * Schedule that performs automatic imports the newest currency exchange rates and saves them to the database.
     *
     * @return Call to the method which performs the import, empty string is the default requestapi URL
     */
    @Scheduled(fixedRate = 14400000, initialDelay = 3600000)
    public String performCurrencyExchangeImport() {
        logger.info("Starting automatic currency exchange import");
        return performCurrencyExchangeImport("");
    }


    /**
     * Perform manual call to the exchangeratesapi.io and import currency exchange rates.
     *
     * @param url URL of the REST API to be called
     * @return Call method which performs the data import.
     */
    public String performCurrencyExchangeImport(String url) {
        ScheduleJobs scheduleJobs = new ScheduleJobs();

        return scheduleJobs.performDailyDatabaseImport(url, this.currencyRepository, this.exchangeRepository);
    }

    /**
     * Schedules the job that performs URL requestapi to keep this and frontend apps running
     */


    @Scheduled(fixedRate = 600000, initialDelay = 600000)
    public void keepTheAppRunning() {
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
     * Schedule job that resets the daily number of available REST API calls. Default is 5 daily uses
     */
    @Scheduled(cron = "0 0 1 * * *", zone = "Europe/Paris")
    public void apiUsagesReset() {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            user.setCurrentRequestsCount(0);
            userRepository.save(user);
        }
        logger.info("Successfully performed reset number of Api uses for every user. Time: " + LocalDateTime.now().toString());
        LocalDateTime.now();
    }
}
