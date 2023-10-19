package dpapps.exchangecurrencyapp.currencytests;

import dpapps.exchangecurrencyapp.exchange.helpers.CurrencyTypesValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
public class CurrencyTypesTests {

    @Test
    void shouldReturnValidCurrency() {
        String currency1 = "EUR";
        String currency2 = "JPY";
        String currency3 = "DKK";

        assertThat(CurrencyTypesValidator.isThisCurrencyAvailable(currency1)).isTrue();
        assertThat(CurrencyTypesValidator.isThisCurrencyAvailable(currency2)).isTrue();
        assertThat(CurrencyTypesValidator.isThisCurrencyAvailable(currency3)).isTrue();
    }

    @Test
    void shouldReturnInvalidCurrency() {
        String currency1 = " USD";
        String currency2 = " EUR";
        String currency3 = "";
        String currency4 = "BBB";
        String currency5 = "USD, EUR";

        assertThat(CurrencyTypesValidator.isThisCurrencyAvailable(currency1)).isFalse();
        assertThat(CurrencyTypesValidator.isThisCurrencyAvailable(currency2)).isFalse();
        assertThat(CurrencyTypesValidator.isThisCurrencyAvailable(currency3)).isFalse();
        assertThat(CurrencyTypesValidator.isThisCurrencyAvailable(currency4)).isFalse();
        assertThat(CurrencyTypesValidator.isThisCurrencyAvailable(currency5)).isFalse();

    }
}
