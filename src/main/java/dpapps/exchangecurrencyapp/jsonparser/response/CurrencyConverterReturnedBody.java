package dpapps.exchangecurrencyapp.jsonparser.response;

import dpapps.exchangecurrencyapp.configuration.AppVariables;
import dpapps.exchangecurrencyapp.exchange.helpers.DecimalPlacesFixer;
import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonConvertable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConverterReturnedBody implements JsonConvertable {
    private String message;

    private LocalDate exchangeDate;
    private Double requestedValue;
    private Double rate;
    private Double calculatedValue;
    private String baseCurrency;
    private String requestedCurrency;

    public void composeMessage() {
        this.message = this.requestedValue + " " + this.baseCurrency + " = " + this.calculatedValue + " " + this.requestedCurrency + "; Date of exchange "+ this.exchangeDate;
    }

    public void calculateValue() {
        double calculated = this.rate * this.requestedValue;
        this.calculatedValue = DecimalPlacesFixer.fix(calculated, AppVariables.DECIMAL_PLACES_CURRENCY_CALCULATION);
    }
}
