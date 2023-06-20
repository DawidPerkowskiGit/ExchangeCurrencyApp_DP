package dpapps.exchangecurrencyapp.jsonparser.jsonentities;

import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.tools.ConversionLocalDateString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JsonExchange {

    private CurrencyRepository currencyRepository;
    private double value;
    private String currency;
    private String exchangeDate;

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

    public String getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(String exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public JsonExchange convertBaseToJson(Exchange exchange) {
        setCurrency(exchange.getCurrency().toString());
        setValue(exchange.getValue());
        setExchangeDate(exchange.getExchangeDate().toString());
        return this;
    }

    public Exchange convertJsonToBase() {
        Exchange exchange = new Exchange();
        exchange.setValue(getValue());
        exchange.setExchangeDate(ConversionLocalDateString.convertStringToLocalDate(getExchangeDate()));
        exchange.setCurrency(currencyRepository.findCurrencyByIsoName(getCurrency()));
        return exchange;
    }
}
