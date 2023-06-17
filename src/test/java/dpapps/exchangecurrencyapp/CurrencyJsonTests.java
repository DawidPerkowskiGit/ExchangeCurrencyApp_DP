package dpapps.exchangecurrencyapp;

import dpapps.exchangecurrencyapp.exchange.entities.Currency;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
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
                new Currency(1, "EUR", "Euro"),
                new Currency(2, "USD", "United States dollar"),
                new Currency(3, "JPY", "Japanese yen")
        );
    }

    @Test
    public void currencySerializationTest() throws IOException {
        Currency currency = currencies[0];
        assertThat(json.write(currency)).isStrictlyEqualToJson("currencySingle.json");
        assertThat(json.write(currency)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(currency)).extractingJsonPathValue("@.id").isEqualTo(1);
        assertThat(json.write(currency)).hasJsonPathValue("@.isoName");
        assertThat(json.write(currency)).extractingJsonPathValue("@.isoName").isEqualTo("EUR");
        assertThat(json.write(currency)).hasJsonPathValue("@.fullName");
        assertThat(json.write(currency)).extractingJsonPathValue("@.fullName").isEqualTo("Euro");
    }

    @Test
    public void currencyDeserializationTest() throws IOException {
        String expected = """
                {
                  "id": 1,
                  "isoName": "EUR",
                  "fullName": "Euro"
                }
                """;
        Currency currency = new Currency(1, "EUR", "Euro");
        assertThat(json.write(currency)).isEqualToJson(expected);
    }

    @Test
    void currencyListSerializationTest() throws IOException {
        assertThat(jsonList.write(currencies)).isStrictlyEqualToJson("currencyList.json");
    }

    @Test
    void currencyListDeserializationTest() throws IOException {
        String expected = """
                [
                  {"id": 1, "isoName": "EUR", "fullName": "Euro"},
                  {"id": 2, "isoName": "USD", "fullName": "United States dollar"},
                  {"id": 3, "isoName": "JPY", "fullName": "Japanese yen"}
                ]
                """;
        assertThat(jsonList.write(currencies)).isEqualToJson(expected);
    }
}
