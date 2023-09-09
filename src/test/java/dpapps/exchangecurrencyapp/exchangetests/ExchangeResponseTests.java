package dpapps.exchangecurrencyapp.exchangetests;

import dpapps.exchangecurrencyapp.exchange.repositories.ApiKeyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
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
    final String validApiKey = "apiKey=UAOrRlJZA2GHGiNmGqP65JTD85_Vv7JUivKp-ibClzZ_vAuj2hqlPHFxhU7LfJDy";
    final String validStartDate = "startDate=2023-08-20";
    final String validFinishDate = "finishDate=2023-08-21";
    final String validCurrency = "currency=PLN";
    final String validBaseCurrency = "baseCurrency=EUR";

    final String noApiKeyJson = "{\"success\":false,\"status\":403,\"message\":\"You did not provide an API KEY\"}";

    final String apiKeyInvalidJson = "{\"success\":false,\"status\":403,\"message\":\"Provided API KEY is invalid\"}";

    final String startDateInvalidJson = "{\"success\":false,\"status\":400,\"message\":\"Cannot perform your request. Invalid start date format\"}";

    final String finishDateInvalidJson = "{\"success\":false,\"status\":400,\"message\":\"Cannot perform your request. Invalid finish date format\"}";

    final String baseCurrencyInvalidJson = "{\"success\":false,\"status\":404,\"message\":\"Cannot perform your request. Base currency is not found\"}";

    final String requestedCurrencyInvalidJson = "{\"success\":false,\"status\":404,\"message\":\"Cannot perform your request. Requested currency is not found\"}";

    List<String> arguments = new LinkedList<>();

    @BeforeEach
    public void emptyArgsList() {
        this.arguments.clear();
    }

    @Test
    public void shouldReturnNoApiKeyProvided() throws Exception {
        String validResponse = noApiKeyJson;

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    @Test
    public void shouldReturnNoApiKeyProvidedVar1() throws Exception {
        String validResponse = noApiKeyJson;

        arguments.add(invalidStartDate);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    @Test
    public void shouldReturnNoApiKeyProvidedVar2() throws Exception {
        String validResponse = noApiKeyJson;

        arguments.add(invalidFinishDate);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    @Test
    public void shouldReturnNoApiKeyProvidedVar3() throws Exception {
        String validResponse = noApiKeyJson;

        arguments.add(invalidCurrency);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    @Test
    public void shouldReturnNoApiKeyProvidedVar4() throws Exception {
        String validResponse = noApiKeyJson;

        arguments.add(invalidBaseCurrency);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }



    @Test
    public void shouldReturnInvalidApiKeyProvided() throws Exception {
        String validResponse = apiKeyInvalidJson;

        arguments.add(invalidApiKey);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    @Test
    public void shouldReturnInvalidStartDate() throws Exception {
        String validResponse = startDateInvalidJson;

        arguments.add(validApiKey);
        arguments.add(invalidStartDate);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    @Test
    public void shouldReturnInvalidFinishDate() throws Exception {
        String validResponse = finishDateInvalidJson;

        arguments.add(validApiKey);
        arguments.add(invalidFinishDate);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    @Test
    public void shouldReturnInvalidBaseCurrency() throws Exception {
        String validResponse = baseCurrencyInvalidJson;

        arguments.add(validApiKey);
        arguments.add(invalidBaseCurrency);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }



    @Test
    public void shouldReturnInvalidRequestedCurrency() throws Exception {
        String validResponse = requestedCurrencyInvalidJson;

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
