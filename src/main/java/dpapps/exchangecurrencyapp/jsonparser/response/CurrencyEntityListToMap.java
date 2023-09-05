package dpapps.exchangecurrencyapp.jsonparser.response;

import dpapps.exchangecurrencyapp.exchange.model.Currency;
import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonConvertable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps currency iso names to their full names.
 * Converts Currency model data to object suitable for JSON conversion
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyEntityListToMap implements JsonConvertable {
    Map<String, String> currencies = new HashMap<>();

    /**
     * Converts Currency model data to Map
     *
     * @param currencyList
     * @return
     */
    public CurrencyEntityListToMap convertCurrencyListToJsonCurrency(Iterable<Currency> currencyList) {

        for (Currency currency : currencyList) {
            currencies.put(currency.getIsoName(), currency.getFullName());
        }

        return this;
    }
}
