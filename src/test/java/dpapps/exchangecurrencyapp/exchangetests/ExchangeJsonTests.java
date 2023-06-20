package dpapps.exchangecurrencyapp.exchangetests;

import dpapps.exchangecurrencyapp.exchange.entities.Currency;
import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.tools.ConversionLocalDateString;
import dpapps.exchangecurrencyapp.jsonparser.jsonentities.JsonExchange;
import dpapps.exchangecurrencyapp.mockrepo.MockCurrencyRepo;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ExchangeJsonTests {

    MockCurrencyRepo currencyRepository = new MockCurrencyRepo();

    @Autowired
    private JacksonTester<JsonExchange> json;

    @Autowired
    private JacksonTester<List<JsonExchange>> jsonList;

    private List<Exchange> exchanges = new ArrayList<>();

    private List<Currency> currencies = new ArrayList<>();

    private List<JsonExchange> jsonExchanges = new ArrayList<>();

    @BeforeEach
    void setUp() {
        currencies.add(new Currency(1, "EUR", "Euro"));
        currencies.add(new Currency(2, "USD", "United States dollar"));
        currencies.add(new Currency(3, "JPY", "Japanese yen"));

        exchanges.add(new Exchange(6, 7.6541 ,  currencies.get(0), ConversionLocalDateString.convertStringToLocalDate("2023-06-08")));
        exchanges.add(new Exchange(7, 37.408 ,  currencies.get(1), ConversionLocalDateString.convertStringToLocalDate("2023-06-10")));
        exchanges.add(new Exchange(8, 1.6061 ,  currencies.get(2), ConversionLocalDateString.convertStringToLocalDate("2023-06-12")));

        jsonExchanges.add(new JsonExchange(currencyRepository).convertBaseToJson(exchanges.get(0)));
        jsonExchanges.add(new JsonExchange(currencyRepository).convertBaseToJson(exchanges.get(1)));
        jsonExchanges.add(new JsonExchange(currencyRepository).convertBaseToJson(exchanges.get(2)));
    }

    @Test
    public void exchangeSerializationTest() throws IOException {
        JsonExchange jsonExchange = jsonExchanges.get(0);
        assertThat(json.write(jsonExchange)).isStrictlyEqualToJson("exchangeSingle.json");
        assertThat(json.write(jsonExchange)).hasJsonPathNumberValue("@.value");
        assertThat(json.write(jsonExchange)).extractingJsonPathValue("@.value").isEqualTo(7.6541);
        assertThat(json.write(jsonExchange)).hasJsonPathValue("@.currency");
        assertThat(json.write(jsonExchange)).extractingJsonPathValue("@.currency").isEqualTo("EUR");
        assertThat(json.write(jsonExchange)).hasJsonPathValue("@.exchangeDate");
        assertThat(json.write(jsonExchange)).extractingJsonPathValue("@.exchangeDate").isEqualTo("2023-06-08");
    }
    @Test
    public void exchnageDeserializationTest() throws IOException {
        String expected = """
                {
                  "exchangeDate": "2023-06-08",
                  "value": 7.6541,
                  "currency": "EUR"
                }
                """;
        JsonExchange jsonExchange = new JsonExchange(currencyRepository).convertBaseToJson(exchanges.get(0));
        assertThat(json.write(jsonExchange)).isEqualToJson(expected);
    }

    @Test
    void exchangeListSerializationTest() throws IOException {
        assertThat(jsonList.write(jsonExchanges)).isStrictlyEqualToJson("exchangeList.json");
    }

    @Test
    void exchangeListDeserializationTest() throws IOException {
        String expected = """
                [
                  {"exchangeDate": "2023-06-08", "value": 7.6541, "currency": "EUR"},
                  {"exchangeDate": "2023-06-10", "value": 37.408, "currency": "USD"},
                  {"exchangeDate": "2023-06-12", "value": 1.6061, "currency": "JPY"}
                ]
                """;
        assertThat(jsonList.write(jsonExchanges)).isEqualToJson(expected);
    }
}
