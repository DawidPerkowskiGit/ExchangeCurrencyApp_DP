package dpapps.exchangecurrencyapp.shedules;

import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This component is responsible for scheduling automatic currency import, keeping app running and resetting daily api requests limit.
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
     */
    @Scheduled(fixedRate = 14400000, initialDelay = 3600000)
    public void performCurrencyExchangeImport() {
        logger.info("Starting automatic currency exchange import");
        ScheduledJobs scheduledJobs = new ScheduledJobs();
        scheduledJobs.performDailyDatabaseImport("", this.currencyRepository, this.exchangeRepository);
    }

    /**
     * Schedules the job that performs URL request to keep app running
     */
    @Scheduled(fixedRate = 600000, initialDelay = 600000)
    public void keepTheAppRunning() {
        ScheduledJobs scheduledJobs = new ScheduledJobs();

        scheduledJobs.performUrlRequestToKeepTheAppRunning();
    }

    /**
     * Schedule job that resets the daily number of available REST API calls. Default is 5 daily uses
     */
    @Scheduled(cron = "0 0 1 * * *", zone = "Europe/Paris")
    public void apiUsagesReset() {
        ScheduledJobs scheduledJobs = new ScheduledJobs();

        scheduledJobs.resetDailyApiUsageCount(this.userRepository);
    }
}
