package dpapps.exchangecurrencyapp;

import dpapps.exchangecurrencyapp.exchange.entities.Currency;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CurrencyJsonTests {
    @Autowired
    private JacksonTester<Currency> json;

    @Autowired
    private JacksonTester<Currency[]> jsonList;

    private Currency[] currencies;

    @BeforeEach
    void setUp() {
        currencies = Arrays.array(
                new Currency(1L, "EUR", "Euro"),
                new Currency(2L, "USD", "United States dollar"),
                new Currency(3L, "JPY", "Japanese yen")
        );
    }

    @Test
    public void currencySerializationTest() throws IOException {
        Currency currency = currencies[0];
        assertThat(json.write(currency)).isStrictlyEqualToJson("singleCurrency.json");
        assertThat(json.write(currency)).hasJsonPathNumberValue("@.currencyId");
        assertThat(json.write(currency)).extractingJsonPathNumberValue("@.currencyId").isEqualTo(1);
    }

}
