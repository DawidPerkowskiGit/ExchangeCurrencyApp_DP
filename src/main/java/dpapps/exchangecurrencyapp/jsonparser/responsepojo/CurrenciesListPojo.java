package dpapps.exchangecurrencyapp.jsonparser.responsepojo;

import dpapps.exchangecurrencyapp.exchange.entities.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//public class CurrenciesListPojo extends ResponsePojo{
//    Map<Integer, String> currencies = new HashMap<>();
//
//    public CurrenciesListPojo convertCurrencyListToJsonCurrency(Iterable<Currency> currencyList) {
//
//        int i = 0;
//        for (Currency currency: currencyList
//             ) {
//            currencies.put(i, currency.getIsoName());
//            i++;
//        }
//
//        return this;
//    }
//}

public class CurrenciesListPojo implements JsonConvertable{
    Map<String, String> currencies = new HashMap<>();

    public CurrenciesListPojo convertCurrencyListToJsonCurrency(Iterable<Currency> currencyList) {

        for (Currency currency: currencyList
        ) {
            currencies.put(currency.getIsoName(), currency.getFullName());
        }

        return this;
    }
}
