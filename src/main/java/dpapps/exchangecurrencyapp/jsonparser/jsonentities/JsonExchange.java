package dpapps.exchangecurrencyapp.jsonparser.jsonentities;

import dpapps.exchangecurrencyapp.exchange.model.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Stores and converts Exchange entity data to JSON format
 */
@Service
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JsonExchange {

    private CurrencyRepository currencyRepository;
    private double value;
    private String currency;
    private String exchangeDate;

    @Autowired
    public JsonExchange(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
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

    public void setExchangeDate(String exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    /**
     * Converts Exchange object to JSON conversion ready object
     *
     * @param exchange Exchange object
     * @return JSON conversion ready Object
     */
    public JsonExchange convertBaseToJson(Exchange exchange) {
        setCurrency(exchange.getCurrency().toString());
        setValue(exchange.getValue());
        setExchangeDate(exchange.getExchangeDate().toString());
        return this;
    }

}
