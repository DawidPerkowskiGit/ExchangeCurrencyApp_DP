package dpapps.exchangecurrencyapp.jsonparser.response.model;

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

}
