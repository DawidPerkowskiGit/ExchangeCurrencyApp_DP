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
        this.mockMvc.perform(get("/currencies")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("[EUR, USD, JPY, BGN, CYP, CZK, DKK, EEK, GBP, HUF, LTL, LVL, MTL, ROL, RON, SEK, SIT, SKK, CHF, NOK, HRK, RUB, TRL, TRY, AUD, BRL, CAD, CNY, HKD, IDR, ILS, INR, KRW, MXN, MYR, NZD, PHP, SGD, THB, ZAR, PLN, ISK]")));
    }

    @Test
    public void shouldReturnOkStatus() throws Exception {
        this.mockMvc.perform(get("/health")).andExpect(status().isOk());
    }
}
