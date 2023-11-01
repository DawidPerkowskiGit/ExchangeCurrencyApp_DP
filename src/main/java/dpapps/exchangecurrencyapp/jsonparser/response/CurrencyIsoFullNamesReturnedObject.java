package dpapps.exchangecurrencyapp.jsonparser.response;

import dpapps.exchangecurrencyapp.exchange.model.Currency;
import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonConvertable;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps currency iso names to their full names.
 * Converts Currency model data to object suitable for JSON conversion
 */
public class CurrencyIsoFullNamesReturnedObject {

    CurrenciesMap currenciesMap;

    public CurrencyIsoFullNamesReturnedObject() {
        this.currenciesMap = new CurrenciesMap();
    }

    /**
     * Converts Currency model list data to Map
     */
    public CurrencyIsoFullNamesReturnedObject convertCurrencyListToJsonCurrency(Iterable<Currency> currencyList) {

        for (Currency currency : currencyList) {
            this.currenciesMap.currencies.put(currency.getIsoName(), currency.getFullName());
        }

        return this;
    }

    public JsonConvertable getCurrenciesMap() {
        return this.currenciesMap;
    }

    private class CurrenciesMap implements JsonConvertable {
        Map<String, String> currencies = new HashMap<>();

        public Map<String, String> getCurrencies() {
            return currencies;
        }
    }
}
