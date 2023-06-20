package dpapps.exchangecurrencyapp.jsonparser.jsonentities;

import dpapps.exchangecurrencyapp.exchange.entities.Currency;
import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.tools.ConversionLocalDateString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JsonExchange {

    private CurrencyRepository currencyRepository;
    private double value;
    private String currency;
    private String exchange_date;

    @Autowired
    public JsonExchange(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public JsonExchange() {
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExchange_date() {
        return exchange_date;
    }

    public void setExchange_date(String exchange_date) {
        this.exchange_date = exchange_date;
    }

    public JsonExchange convertBaseToJson(Exchange exchange) {
        setCurrency(exchange.getCurrency().toString());
        setValue(exchange.getValue());
        setExchange_date(exchange.getExchangeDate().toString());
        return this;
    }

    public Exchange convertJsonToBase() {
        Exchange exchange = new Exchange();
        exchange.setValue(getValue());
        exchange.setExchangeDate(ConversionLocalDateString.convertStringToLocalDate(getExchange_date()));
        exchange.setCurrency(currencyRepository.findCurrencyByIsoName(getCurrency()));
        return exchange;
    }
}
