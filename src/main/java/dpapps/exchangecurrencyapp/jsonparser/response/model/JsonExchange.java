package dpapps.exchangecurrencyapp.jsonparser.response.model;

import dpapps.exchangecurrencyapp.exchange.model.Exchange;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

/**
 * Converts database Exchange entry to exchange object returned by REST API
 */
@Service
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JsonExchange {

    private double value;
    private String currency;
    private String exchangeDate;

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

    public JsonExchange convertBaseToJson(Exchange exchange) {
        setCurrency(exchange.getCurrency().toString());
        setValue(exchange.getValue());
        setExchangeDate(exchange.getExchangeDate().toString());
        return this;
    }

}
