package dpapps.exchangecurrencyapp.exchange.controllers;

import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
@RestController
public class ExchangeController {

    private final ExchangeRepository exchangeRepository;

    private final CurrencyRepository currencyRepository;

    @Autowired
    public ExchangeController(ExchangeRepository constructorExchangeRepository, CurrencyRepository constructorCurrencyRepository) {
        this.exchangeRepository = constructorExchangeRepository;
        this.currencyRepository = constructorCurrencyRepository;
    }

    @GetMapping("/currencies")
    public String getCurrencies() {
        return currencyRepository.findAll().toString();
    }

}
