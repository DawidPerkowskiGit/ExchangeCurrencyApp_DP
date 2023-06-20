package dpapps.exchangecurrencyapp;

import dpapps.exchangecurrencyapp.exchange.entities.Currency;
import dpapps.exchangecurrencyapp.jsonparser.jsonentities.JsonCurrency;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CurrencyJsonTests {
    @Autowired
    private JacksonTester<JsonCurrency> json;

    @Autowired
    private JacksonTester<List<JsonCurrency>> jsonList;

    private List<JsonCurrency> currencies = new ArrayList<>();

    @BeforeEach
    void setUp() {
        currencies.add(new JsonCurrency("EUR", "Euro"));
        currencies.add(new JsonCurrency("USD", "United States dollar"));
        currencies.add(new JsonCurrency("JPY", "Japanese yen"));
    }

    @Test
    public void currencySerializationTest() throws IOException {
        JsonCurrency currency = currencies.get(0);
        assertThat(json.write(currency)).isStrictlyEqualToJson("currencySingle.json");
        assertThat(json.write(currency)).hasJsonPathValue("@.isoName");
        assertThat(json.write(currency)).extractingJsonPathValue("@.isoName").isEqualTo("EUR");
        assertThat(json.write(currency)).hasJsonPathValue("@.fullName");
        assertThat(json.write(currency)).extractingJsonPathValue("@.fullName").isEqualTo("Euro");
    }

    @Test
    public void currencyDeserializationTest() throws IOException {
        String expected = """
                {
                  "isoName": "EUR",
                  "fullName": "Euro"
                }
                """;
        JsonCurrency currency = new JsonCurrency("EUR", "Euro");
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
                  {"isoName": "EUR", "fullName": "Euro"},
                  {"isoName": "USD", "fullName": "United States dollar"},
                  {"isoName": "JPY", "fullName": "Japanese yen"}
                ]
                """;
        assertThat(jsonList.write(currencies)).isEqualToJson(expected);
    }
}
