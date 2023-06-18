package dpapps.exchangecurrencyapp.configuration;

import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.LocationCurrencyPairRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;

@org.springframework.context.annotation.Configuration
@EnableScheduling
public class Configuration {
    private final ExchangeRepository exchangeRepository;

    private final CurrencyRepository currencyRepository;

    private final LocationRepository locationRepository;

    private final LocationCurrencyPairRepository locationCurrencyPairRepository;

    @Autowired
    public Configuration(ExchangeRepository exchangeRepository, CurrencyRepository currencyRepository, LocationRepository locationRepository, LocationCurrencyPairRepository locationCurrencyPairRepository) {
        this.exchangeRepository = exchangeRepository;
        this.currencyRepository = currencyRepository;
        this.locationRepository = locationRepository;
        this.locationCurrencyPairRepository = locationCurrencyPairRepository;
    }
}
