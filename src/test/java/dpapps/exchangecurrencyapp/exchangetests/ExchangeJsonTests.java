package dpapps.exchangecurrencyapp.exchangetests;

import com.fasterxml.jackson.databind.ObjectMapper;
import dpapps.exchangecurrencyapp.exchange.model.Currency;
import dpapps.exchangecurrencyapp.exchange.model.Exchange;
import dpapps.exchangecurrencyapp.exchange.service.JsonBuilderService;
import dpapps.exchangecurrencyapp.exchange.service.JsonBuilderServiceImpl;
import dpapps.exchangecurrencyapp.exchange.tools.LocalDateStringConverter;
import dpapps.exchangecurrencyapp.jsonparser.response.SingleDayExchangeRatesJson;
import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonExchange;
import dpapps.exchangecurrencyapp.mockrepo.MockCurrencyRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ExchangeJsonTests {

    @Autowired
    private JacksonTester<SingleDayExchangeRatesJson> json;


    private List<Exchange> exchanges = new ArrayList<>();

    private List<Currency> currencies = new ArrayList<>();

    private SingleDayExchangeRatesJson jsonExchanges = new SingleDayExchangeRatesJson();


    @BeforeEach
    void setUp() {
        currencies.add(new Currency(1, "EUR", "Euro"));
        currencies.add(new Currency(2, "USD", "United States dollar"));
        currencies.add(new Currency(3, "JPY", "Japanese yen"));

        exchanges.add(new Exchange(6, 7.6541 ,  currencies.get(0), LocalDateStringConverter.convertStringToLocalDate("2023-06-08")));
        exchanges.add(new Exchange(7, 37.408 ,  currencies.get(1), LocalDateStringConverter.convertStringToLocalDate("2023-06-08")));
        exchanges.add(new Exchange(8, 1.6061 ,  currencies.get(2), LocalDateStringConverter.convertStringToLocalDate("2023-06-08")));

        jsonExchanges.setSuccess(true);
        jsonExchanges.setDate(exchanges.get(0).getExchangeDate());
        jsonExchanges.setBase("AUD");

        Map<String, Double> exchangesMap = new LinkedHashMap<>();

        for (Exchange exchange: exchanges
             ) {
            exchangesMap.put(exchange.getCurrency().getIsoName(), exchange.getValue());
        }

        jsonExchanges.setRates(exchangesMap);
    }

    @Test
    public void exchangeSerializationTest() throws IOException {
        assertThat(json.write(jsonExchanges)).isStrictlyEqualToJson("exchangeSingle.json");
    }
    @Test
    public void exchangeDeserializationTest() throws IOException {
        String expected = """
                {
                  "success": true,
                  "date": "2023-06-08",
                  "base": "AUD",
                  "rates": {
                    "EUR": 7.6541,
                    "USD": 37.408,
                    "JPY": 1.6061
                  }
                }
                """;
        ObjectMapper mapper = new ObjectMapper();
        SingleDayExchangeRatesJson singleDayExchangeRatesJson = mapper.readValue(expected, SingleDayExchangeRatesJson.class);
        assertThat(Objects.equals(singleDayExchangeRatesJson, jsonExchanges));
    }
}
