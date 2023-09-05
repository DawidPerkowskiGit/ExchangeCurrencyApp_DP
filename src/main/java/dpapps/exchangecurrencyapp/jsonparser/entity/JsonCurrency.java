package dpapps.exchangecurrencyapp.jsonparser.entity;

import dpapps.exchangecurrencyapp.exchange.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Stores and converts Currency entity data to JSON format
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JsonCurrency {
    private String isoName;
    private String fullName;

    public void setIsoName(String isoName) {
        this.isoName = isoName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Converts Currency to JsonCurrency
     *
     * @param currency Currency entity object
     * @return Currency object prepared to be converted to JSON
     */
    public JsonCurrency convertCurrencyToJsonCurrency(Currency currency) {
        setIsoName(currency.getIsoName());
        setFullName(currency.getFullName());
        return this;
    }

    public JsonCurrency(Currency currency) {
        convertCurrencyToJsonCurrency(currency);
    }
}
