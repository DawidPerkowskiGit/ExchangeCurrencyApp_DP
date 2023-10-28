package dpapps.exchangecurrencyapp.jsonparser.response.model;

import dpapps.exchangecurrencyapp.configuration.AppConstants;
import dpapps.exchangecurrencyapp.exchange.helpers.DecimalPlacesFixer;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

/**
 * Model
 */
@AllArgsConstructor
public class ConvertedCurrencyReturnedObject implements JsonConvertable {

    ConversionBody conversionBody;

    public ConvertedCurrencyReturnedObject(LocalDate date, Double requestedValue, Double rate, String baseCurrency, String requestedCurrency) {
        conversionBody = new ConversionBody();
        conversionBody.setExchangeDate(date);
        conversionBody.setRequestedValue(requestedValue);
        conversionBody.setRate(rate);
        conversionBody.setBaseCurrency(baseCurrency);
        conversionBody.setRequestedCurrency(requestedCurrency);
        calculateValue();
        composeMessage();
    }


    public void composeMessage() {
        this.conversionBody.setMessage(conversionBody.requestedValue + " " + conversionBody.baseCurrency + " = " + conversionBody.calculatedValue + " " + conversionBody.requestedCurrency + "; Date of exchange " + conversionBody.exchangeDate);
    }

    public void calculateValue() {
        double calculated = conversionBody.rate * conversionBody.requestedValue;
        this.conversionBody.setCalculatedValue(DecimalPlacesFixer.modifyNumberOfDecimalPlaces(calculated, AppConstants.DECIMAL_PLACES_CURRENCY_CALCULATION));
    }

    public JsonConvertable getBody() {
        return this.conversionBody;
    }


    class ConversionBody implements JsonConvertable {
        private String message;
        private LocalDate exchangeDate;
        private Double requestedValue;
        private Double rate;
        private Double calculatedValue;
        private String baseCurrency;
        private String requestedCurrency;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public LocalDate getExchangeDate() {
            return exchangeDate;
        }

        public void setExchangeDate(LocalDate exchangeDate) {
            this.exchangeDate = exchangeDate;
        }

        public Double getRequestedValue() {
            return requestedValue;
        }

        public void setRequestedValue(Double requestedValue) {
            this.requestedValue = requestedValue;
        }

        public Double getRate() {
            return rate;
        }

        public void setRate(Double rate) {
            this.rate = rate;
        }

        public Double getCalculatedValue() {
            return calculatedValue;
        }

        public void setCalculatedValue(Double calculatedValue) {
            this.calculatedValue = calculatedValue;
        }

        public String getBaseCurrency() {
            return baseCurrency;
        }

        public void setBaseCurrency(String baseCurrency) {
            this.baseCurrency = baseCurrency;
        }

        public String getRequestedCurrency() {
            return requestedCurrency;
        }

        public void setRequestedCurrency(String requestedCurrency) {
            this.requestedCurrency = requestedCurrency;
        }
    }
}
