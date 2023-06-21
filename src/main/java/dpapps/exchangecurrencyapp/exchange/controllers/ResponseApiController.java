package dpapps.exchangecurrencyapp.exchange.controllers;

import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.LocationCurrencyPairRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.LocationRepository;
import dpapps.exchangecurrencyapp.jsonparser.responseexchanges.ResponseBodyObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ResponseApiController {
    CurrencyRepository currencyRepository;

    ExchangeRepository exchangeRepository;

    LocationRepository locationRepository;

    LocationCurrencyPairRepository locationCurrencyPairRepository;

    @Autowired
    public ResponseApiController(CurrencyRepository currencyRepository, ExchangeRepository exchangeRepository, LocationRepository locationRepository, LocationCurrencyPairRepository locationCurrencyPairRepository) {
        this.currencyRepository = currencyRepository;
        this.exchangeRepository = exchangeRepository;
        this.locationRepository = locationRepository;
        this.locationCurrencyPairRepository = locationCurrencyPairRepository;
    }

    @GetMapping("/latest")
    public Optional<ResponseBodyObject> getLatestExchange() {
        ResponseBodyObject responseBodyObject = new ResponseBodyObject();
        try {
            LocalDate latestExchangeDate = exchangeRepository.returnValidLatestExchangeData();
            List<Exchange> latestExchangesList = exchangeRepository.findAllByExchangeDate(latestExchangeDate);
            Map<String, Double> rates = new HashMap<>();
            for (Exchange entry: latestExchangesList
                 ) {
                rates.put(entry.getCurrency().getIsoName(), entry.getValue());
            }


        }
        catch (Exception e) {
            System.out.println("Could not acquire latest exchange data: Exception: "+ e);
        }

        return Optional.ofNullable(responseBodyObject);
    }
}
