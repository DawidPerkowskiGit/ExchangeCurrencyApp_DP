package dpapps.exchangecurrencyapp.currencytests;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnCurrenciesList() throws Exception {
        String validResponse = "{\"currencies\":{\"CHF\":\"Swiss franc\",\"HRK\":\"Croatian kuna\",\"MXN\":\"Mexican peso\",\"LVL\":\"Latvian lats\",\"MTL\":\"Maltese lira\",\"LTL\":\"Lithuanian litas\",\"ZAR\":\"South African rand\",\"INR\":\"Indian rupee\",\"CNY\":\"Chinese yuan renminbi\",\"THB\":\"Thai baht\",\"TRL\":\"Turkish lira\",\"AUD\":\"Australian dollar\",\"ILS\":\"Israeli new shekel\",\"KRW\":\"South Korean won\",\"JPY\":\"Japanese yen\",\"PLN\":\"Polish zloty\",\"GBP\":\"Pound sterling\",\"IDR\":\"Indonesian rupiah\",\"HUF\":\"Hungarian forint\",\"PHP\":\"Philippine peso\",\"TRY\":\"Turkish lira\",\"CYP\":\"Cypriot pound\",\"RUB\":\"Russian ruble\",\"HKD\":\"Hong Kong dollar\",\"ISK\":\"Icelandic krona\",\"EUR\":\"Euro\",\"DKK\":\"Danish krone\",\"CAD\":\"Canadian dollar\",\"MYR\":\"Malaysian ringgit\",\"USD\":\"United States dollar\",\"BGN\":\"Bulgarian lev\",\"EEK\":\"Estonian kroon\",\"NOK\":\"Norwegian krone\",\"ROL\":\"Romanian leu\",\"RON\":\"Romanian leu\",\"SGD\":\"Singapore dollar\",\"SKK\":\"Slovak koruna\",\"CZK\":\"Czech koruna\",\"SEK\":\"Swedish krona\",\"NZD\":\"New Zealand dollar\",\"BRL\":\"Brazilian real\",\"SIT\":\"Slovenian tolar\"}}";
        this.mockMvc.perform(get("/api/currencies")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    @Test
    public void shouldReturnOkStatus() throws Exception {
        this.mockMvc.perform(get("/health")).andExpect(status().isOk());
    }
}
