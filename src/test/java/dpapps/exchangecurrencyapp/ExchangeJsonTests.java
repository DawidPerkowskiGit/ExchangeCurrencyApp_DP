package dpapps.exchangecurrencyapp;

import dpapps.exchangecurrencyapp.exchange.entities.Currency;
import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import dpapps.exchangecurrencyapp.exchange.tools.ConversionLocalDateString;
import dpapps.exchangecurrencyapp.jsonparser.jsonentities.JsonExchange;
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

    CurrencyRepository currencyRepository;

    @Autowired
    private JacksonTester<JsonExchange> json;

    @Autowired
    private JacksonTester<Exchange[]> jsonList;

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

        jsonExchanges.add(new JsonExchange().convertBaseToJson(exchanges.get(0)));
        jsonExchanges.add(new JsonExchange().convertBaseToJson(exchanges.get(1)));
        jsonExchanges.add(new JsonExchange().convertBaseToJson(exchanges.get(2)));
    }

    @Test
    public void exchangeSerializationTest() throws IOException {
        JsonExchange jsonExchange = jsonExchanges.get(0);
        assertThat(json.write(jsonExchange)).isStrictlyEqualToJson("exchangeSingle.json");
        assertThat(json.write(jsonExchange)).hasJsonPathNumberValue("@.value");
        assertThat(json.write(jsonExchange)).extractingJsonPathValue("@.value").isEqualTo(7.6541);
        assertThat(json.write(jsonExchange)).hasJsonPathValue("@.currency");
        assertThat(json.write(jsonExchange)).extractingJsonPathValue("@.currency").isEqualTo("EUR");
        assertThat(json.write(jsonExchange)).hasJsonPathValue("@.exchange_date");
        assertThat(json.write(jsonExchange)).extractingJsonPathValue("@.exchange_date").isEqualTo("2023-06-08");
    }
//    @Test
//    public void currencyDeserializationTest() throws IOException {
//        String expected = """
//                {
//                  "id": 1,
//                  "isoName": "EUR",
//                  "fullName": "Euro"
//                }
//                """;
//        Currency currency = new Currency(1, "EUR", "Euro");
//        assertThat(json.write(currency)).isEqualToJson(expected);
//    }
//
//    @Test
//    void currencyListSerializationTest() throws IOException {
//        assertThat(jsonList.write(exchanges)).isStrictlyEqualToJson("currencyList.json");
//    }
//
//    @Test
//    void currencyListDeserializationTest() throws IOException {
//        String expected = """
//                [
//                  {"id": 1, "isoName": "EUR", "fullName": "Euro"},
//                  {"id": 2, "isoName": "USD", "fullName": "United States dollar"},
//                  {"id": 3, "isoName": "JPY", "fullName": "Japanese yen"}
//                ]
//                """;
//        assertThat(jsonList.write(exchanges)).isEqualToJson(expected);
//    }
}
