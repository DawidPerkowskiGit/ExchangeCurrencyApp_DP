package dpapps.exchangecurrencyapp.jsonparser.jsonentities;

import dpapps.exchangecurrencyapp.exchange.entities.Currency;

public class JsonCurrency {
    private String isoName;

    private String fullName;

    public JsonCurrency(String isoName, String fullName) {
        this.isoName = isoName;
        this.fullName = fullName;
    }

    public String getIsoName() {
        return isoName;
    }

    public void setIsoName(String isoName) {
        this.isoName = isoName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Currency convertJsonCurrencyToCurrency() {
        Currency currency = new Currency() {};
        currency.setIsoName(getIsoName());
        currency.setFullName(getFullName());
        return currency;
    }

    public JsonCurrency convertCurrencyToJsonCurrency(Currency currency) {
        setIsoName(currency.getIsoName());
        setFullName(currency.getFullName());
        return this;
    }

    public JsonCurrency(Currency currency) {
        convertCurrencyToJsonCurrency(currency);
    }
}
