package dpapps.exchangecurrencyapp.exchangetests;

import dpapps.exchangecurrencyapp.exchange.model.Currency;
import dpapps.exchangecurrencyapp.exchange.model.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.ApiKeyRepository;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;
import dpapps.exchangecurrencyapp.exchange.service.ExchangeService;
import dpapps.exchangecurrencyapp.mockrepo.MockApiKeyRepo;
import dpapps.exchangecurrencyapp.mockrepo.MockExchangeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ExchangeResponseTests {
    @Autowired
    private MockMvc mockMvc;

    final String baseUrl = "/api/exchange";
    final String invalidApiKey = "apiKey=1234";
    final String invalidStartDate = "startDate=2023-18-20";
    final String invalidFinishDate = "finishDate=2023-18-21";
    final String invalidCurrency = "currency=PPP";
    final String invalidBaseCurrency = "baseCurrency=TTT";
    final String validApiKey = "apiKey=jHZZGOiui3sQfjlsWTS-6lnJuT4dBT_QJnE__DB8iCoKDp9qFskh1QivzeAqP5Fo";
    final String validStartDate = "startDate=2023-08-20";
    final String validFinishDate = "finishDate=2023-08-21";
    final String validCurrency = "currency=PLN";
    final String validBaseCurrency = "baseCurrency=EUR";

    List<String> arguments = new LinkedList<>();

    final Map<String, String> headers = new LinkedHashMap<>();
    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @BeforeEach
    public void emptyArgsList() {
        this.arguments.clear();
    }

    @Test
    public void shouldReturnNoApiKeyProvided() throws Exception {
        String validResponse = "{\"success\":false,\"status\":403,\"message\":\"You did not provide an API KEY\"}";

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    @Test
    public void shouldReturnInvalidApiKeyProvided() throws Exception {
        String validResponse = "{\"success\":false,\"status\":403,\"message\":\"Provided API KEY is invalid\"}";

        arguments.add(invalidApiKey);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    @Test
    public void shouldReturnInvalidStartDate() throws Exception {
        String validResponse = "{\"success\":false,\"status\":400,\"message\":\"Cannot perform your request. Invalid start date format\"}";

        arguments.add(validApiKey);
        arguments.add(invalidStartDate);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    @Test
    public void shouldReturnInvalidFinishDate() throws Exception {
        String validResponse = "{\"success\":false,\"status\":400,\"message\":\"Cannot perform your request. Invalid finish date format\"}";

        arguments.add(validApiKey);
        arguments.add(invalidFinishDate);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    @Test
    public void shouldReturnInvalidBaseCurrency() throws Exception {
        String validResponse = "{\"success\":false,\"status\":404,\"message\":\"Cannot perform your request. Base currency is not found\"}";

        arguments.add(validApiKey);
        arguments.add(invalidBaseCurrency);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    @Test
    public void shouldReturnInvalidRequestedCurrency() throws Exception {
        String validResponse = "{\"success\":false,\"status\":404,\"message\":\"Cannot perform your request. Requested currency is not found\"}";

        arguments.add(validApiKey);
        arguments.add(invalidCurrency);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }


    public String urlBuilder() {
        StringBuilder returnedUrl = new StringBuilder(baseUrl);

        if (arguments.isEmpty()) {
            return returnedUrl.toString();
        }

        returnedUrl.append("?");

        for (int i = 0; i < arguments.size(); i++) {
            returnedUrl.append(arguments.get(i));
            if (i + 1 < arguments.size()) {
                returnedUrl.append("&");
            }
        }

        return returnedUrl.toString();
    }
}
