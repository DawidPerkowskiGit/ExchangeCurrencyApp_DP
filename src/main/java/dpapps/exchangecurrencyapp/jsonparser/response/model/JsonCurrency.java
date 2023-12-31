package dpapps.exchangecurrencyapp.jsonparser.response.model;

import dpapps.exchangecurrencyapp.exchange.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Converts database Currency entry to currency object returned by REST API
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JsonCurrency {
    private String isoName;
    private String fullName;

    public JsonCurrency(Currency currency) {
        convertCurrencyToJsonCurrency(currency);
    }

    public void setIsoName(String isoName) {
        this.isoName = isoName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private void convertCurrencyToJsonCurrency(Currency currency) {
        setIsoName(currency.getIsoName());
        setFullName(currency.getFullName());
    }
}
