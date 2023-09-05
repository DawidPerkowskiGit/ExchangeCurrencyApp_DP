package dpapps.exchangecurrencyapp.currencytests;

import dpapps.exchangecurrencyapp.exchange.model.Currency;
import dpapps.exchangecurrencyapp.jsonparser.entity.JsonCurrency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    private JacksonTester<JsonCurrency> jacksonJsonCurrency;

    @Autowired
    private JacksonTester<List<JsonCurrency>> jacksonJsonCurrencyList;

    private List<JsonCurrency> jsonCurrencyList = new ArrayList<>();

    @Autowired
    private JacksonTester<Currency> jacksonCurrency;

    @Autowired
    private JacksonTester<List<Currency>> jacksonCurrencyList;

    private List<Currency> currencyList = new ArrayList<>();

    @BeforeEach
    void setUp() {


        currencyList.add(new Currency(1, "EUR", "Euro"));
        currencyList.add(new Currency(2, "USD", "United States dollar"));
        currencyList.add(new Currency(3, "JPY", "Japanese yen"));

        jsonCurrencyList.add(new JsonCurrency(currencyList.get(0)));
        jsonCurrencyList.add(new JsonCurrency(currencyList.get(1)));
        jsonCurrencyList.add(new JsonCurrency(currencyList.get(2)));
    }

    @Test
    public void currencySerializationTest() throws IOException {
        JsonCurrency currency = jsonCurrencyList.get(0);
        assertThat(jacksonJsonCurrency.write(currency)).isStrictlyEqualToJson("currencySingle.json");
        assertThat(jacksonJsonCurrency.write(currency)).hasJsonPathValue("@.isoName");
        assertThat(jacksonJsonCurrency.write(currency)).extractingJsonPathValue("@.isoName").isEqualTo("EUR");
        assertThat(jacksonJsonCurrency.write(currency)).hasJsonPathValue("@.fullName");
        assertThat(jacksonJsonCurrency.write(currency)).extractingJsonPathValue("@.fullName").isEqualTo("Euro");
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
        assertThat(jacksonJsonCurrency.write(currency)).isEqualToJson(expected);
    }

    @Test
    void currencyListSerializationTest() throws IOException {
        assertThat(jacksonJsonCurrencyList.write(jsonCurrencyList)).isStrictlyEqualToJson("currencyList.json");
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
        assertThat(jacksonJsonCurrencyList.write(jsonCurrencyList)).isEqualToJson(expected);
    }
}
