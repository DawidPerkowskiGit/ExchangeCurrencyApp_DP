package dpapps.exchangecurrencyapp.jsonparser.response;

import dpapps.exchangecurrencyapp.jsonparser.response.model.SingleDayExchangeRatesJson;

public class ObjectComparator {
    public boolean compareSingleDayExchange(SingleDayExchangeRatesJson object1, SingleDayExchangeRatesJson object2) {
        return object1.isSuccess() == object1.isSuccess() && object1.getBase().equals(object2.getBase()) && object1.getDate().isEqual(object2.getDate()) && object1.getRates().equals(object2.getRates());

    }
}
