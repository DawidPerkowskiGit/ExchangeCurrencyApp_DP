package dpapps.exchangecurrencyapp.jsonparser.requestapi;

import dpapps.exchangecurrencyapp.exchange.entities.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps currency iso names to their full names.
 * Converts Currency entity data to object suitable for JSON conversion
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyListPojo implements JsonConvertable {
    Map<String, String> currencies = new HashMap<>();

    /**
     * Converts Currency entity data to Map
     *
     * @param currencyList
     * @return
     */
    public CurrencyListPojo convertCurrencyListToJsonCurrency(Iterable<Currency> currencyList) {

        for (Currency currency : currencyList) {
            currencies.put(currency.getIsoName(), currency.getFullName());
        }

        return this;
    }
}
