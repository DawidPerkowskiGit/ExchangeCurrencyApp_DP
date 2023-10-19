package dpapps.exchangecurrencyapp.currencytests;

import dpapps.exchangecurrencyapp.exchange.helpers.CurrencyListParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CurrencyListParserTests {
    final String emptyString ="";

    final String singleCurrency = "USD";

    final String validCurrencies = "EUR,PLN,USD";

    final String oneInvalidCurrencyInList = "EURR,PLN,USD";

    final String gibberish = "sdfsf,dsfsdf.dsf,sdfd.ffdfdf";

    final String nullString = null;

    @Test
    void shouldReturnEmptyList() {
        assertThat(CurrencyListParser.parseCurrencyList(emptyString)).isEmpty();
    }

    @Test
    void shouldReturnListWithSingleElement() {
        List<String> listOneElement = new LinkedList<>();
        listOneElement.add("USD");
        assertThat(CurrencyListParser.parseCurrencyList(singleCurrency).get(0)).isEqualTo(listOneElement.get(0));
    }

    @Test
    void shouldReturnCompleteCurrencyList() {
        List<String> listOfMultipleCurrencies = new LinkedList<>();
        listOfMultipleCurrencies.add("EUR");
        listOfMultipleCurrencies.add("PLN");
        listOfMultipleCurrencies.add("USD");

        List<String> extractedList = CurrencyListParser.parseCurrencyList(validCurrencies);
        for (int i = 0; i < listOfMultipleCurrencies.size(); i++) {
            assertThat(listOfMultipleCurrencies.get(i)).isEqualTo(extractedList.get(i));
        }
    }

    @Test
    void shouldReturnIncompleteCurrencyList() {
        List<String> listOfMultipleCurrencies = new LinkedList<>();
        listOfMultipleCurrencies.add("PLN");
        listOfMultipleCurrencies.add("USD");

        List<String> extractedList = CurrencyListParser.parseCurrencyList(oneInvalidCurrencyInList);
        for (int i = 0; i < listOfMultipleCurrencies.size(); i++) {
            assertThat(listOfMultipleCurrencies.get(i)).isEqualTo(extractedList.get(i));
        }
    }

    @Test
    void shouldReturnEmptyListWhenNoCurrencyFound() {
        assertThat(CurrencyListParser.parseCurrencyList(gibberish)).isEmpty();
    }

    @Test
    void shouldReturnEntryWithEmptyString() {
        assertThat(CurrencyListParser.parseCurrencyList(nullString).get(0)).isEqualTo(emptyString);
    }

}
