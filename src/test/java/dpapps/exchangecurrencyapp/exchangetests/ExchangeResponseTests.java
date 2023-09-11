package dpapps.exchangecurrencyapp.exchangetests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests the /api/exchange endpoint requests. Tests are performed in the same order, as URL parameters are checked for validation in the service.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ExchangeResponseTests {
    @Autowired
    private MockMvc mockMvc;

    final String baseUrl = "/api/exchange";
    final String invalidApiKey = "apiKey=1234";
    final String invalidCurrency = "currency=PPP";
    final String invalidBaseCurrency = "baseCurrency=TTT";
    final String invalidStartDate = "startDate=2023-18-20";
    final String invalidFinishDate = "finishDate=2023-18-21";

    final String validApiKey = "apiKey=UAOrRlJZA2GHGiNmGqP65JTD85_Vv7JUivKp-ibClzZ_vAuj2hqlPHFxhU7LfJDy";
    final String validCurrency = "currency=PLN";
    final String validBaseCurrency = "baseCurrency=EUR";
    final String validStartDate = "startDate=2023-08-20";
    final String validFinishDate = "finishDate=2023-08-21";

    final String emptyApiKeyArg = "apiKey=";
    final String emptyRequestedCurrencyArg = "baseCurrency=";
    final String emptyBaseCurrencyArg = "currency=";
    final String emptyStartDateArg = "startDate=";
    final String emptyFinishDateArg = "finishDate=";

    final String noApiKeyJson = "{\"success\":false,\"status\":403,\"message\":\"You did not provide an API KEY\"}";
    final String apiKeyInvalidJson = "{\"success\":false,\"status\":403,\"message\":\"Provided API KEY is invalid\"}";
    final String requestedCurrencyInvalidJson = "{\"success\":false,\"status\":404,\"message\":\"Cannot perform your request. Requested currency is not found\"}";
    final String baseCurrencyInvalidJson = "{\"success\":false,\"status\":404,\"message\":\"Cannot perform your request. Base currency is not found\"}";
    final String startDateInvalidJson = "{\"success\":false,\"status\":400,\"message\":\"Cannot perform your request. Invalid start date format\"}";
    final String finishDateInvalidJson = "{\"success\":false,\"status\":400,\"message\":\"Cannot perform your request. Invalid finish date format\"}";

    List<String> arguments = new LinkedList<>();
    private final int argsListCount = 4;
    String[] apiKeyArgs = new String[argsListCount];
    String[] requestedCurrencyArgs = new String[argsListCount];
    String[] baseCurrencyArgs = new String[argsListCount];
    String[] startDateArgs = new String[argsListCount];
    String[] finishDateArgs = new String[argsListCount];
    String emptyString = "";
    String nullField = null;

    ExchangeResponseTests() {
        this.apiKeyArgs[0] = emptyApiKeyArg;
        this.apiKeyArgs[1] = invalidApiKey;
        this.apiKeyArgs[2] = validApiKey;
        this.apiKeyArgs[3] = nullField;

        this.requestedCurrencyArgs[0] = emptyRequestedCurrencyArg;
        this.requestedCurrencyArgs[1] = invalidCurrency;
        this.requestedCurrencyArgs[2] = validCurrency;
        this.requestedCurrencyArgs[3] = nullField;

        this.baseCurrencyArgs[0] = emptyBaseCurrencyArg;
        this.baseCurrencyArgs[1] = invalidBaseCurrency;
        this.baseCurrencyArgs[2] = validBaseCurrency;
        this.baseCurrencyArgs[3] = nullField;

        this.startDateArgs[0] = emptyStartDateArg;
        this.startDateArgs[1] = invalidStartDate;
        this.startDateArgs[2] = validStartDate;
        this.startDateArgs[3] = nullField;

        this.finishDateArgs[0] = emptyFinishDateArg;
        this.finishDateArgs[1] = invalidFinishDate;
        this.finishDateArgs[2] = validFinishDate;
        this.finishDateArgs[3] = nullField;
    }



    @BeforeEach
    public void emptyArgsList() {
        this.arguments.clear();
    }

    /**
     * Tests single argument endpoint request where no API KEY was provided.
     * @throws Exception MockMVC Exception
     */
    @Test
    public void shouldReturnNoApiKeyProvided() throws Exception {
        String validResponse = noApiKeyJson;

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    /**
     * Tests all other arguments permutations endpoint request where no API KEY was provided.
     * @throws Exception MockMVC Exception
     */

    @Test
    public void shouldReturnNoApiKeyProvidedWithAllArgsPermutations() throws Exception {
        String validResponse = noApiKeyJson;

        List<String> urlList = multipleArgsCombinationsBuilder(null, requestedCurrencyArgs, baseCurrencyArgs, startDateArgs, finishDateArgs);

        int counter = 0;

        for (String url: urlList
             ) {
            System.out.println("Testing URL: "+ url);
            this.mockMvc.perform(get(url)).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString(validResponse)));
            counter++;
        }

        System.out.println("Performed API requests for " + counter + " URL permutations");
    }

    /**
     * Tests single argument endpoint request where invalid API KEY was provided.
     * @throws Exception MockMVC Exception
     */
    @Test
    public void shouldReturnInvalidApiKeyProvided() throws Exception {
        String validResponse = apiKeyInvalidJson;

        arguments.add(invalidApiKey);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    /**
     * Tests all other arguments permutations endpoint request where invalid API KEY was provided.
     * @throws Exception MockMVC Exception
     */

    @Test
    public void shouldReturnInvalidApiKeyProvidedWithAllArgsPermutations() throws Exception {
        String validResponse = apiKeyInvalidJson;

        List<String> urlList = multipleArgsCombinationsBuilder(invalidApiKey, requestedCurrencyArgs, baseCurrencyArgs, startDateArgs, finishDateArgs);

        int counter = 0;

        for (String url: urlList
        ) {
            System.out.println("Testing URL: "+ url);
            this.mockMvc.perform(get(url)).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString(validResponse)));
            counter++;
        }

        System.out.println("Performed API requests for " + counter + " URL permutations");
    }

    /**
     * Tests single argument endpoint request where invalid requested currency was provided.
     * @throws Exception MockMVC Exception
     */

    @Test
    public void shouldReturnInvalidRequestedCurrency() throws Exception {
        String validResponse = requestedCurrencyInvalidJson;

        arguments.add(validApiKey);
        arguments.add(invalidCurrency);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }
    /**
     * Tests all other arguments permutations endpoint request where invalid requested currency was provided.
     * @throws Exception MockMVC Exception
     */
    @Test
    public void shouldReturnInvalidRequestedCurrencyWithAllOtherArgs() throws Exception {
        String validResponse = requestedCurrencyInvalidJson;

        List<String> urlList = multipleArgsCombinationsBuilder(validApiKey, invalidCurrency, baseCurrencyArgs, startDateArgs, finishDateArgs);

        int counter = 0;

        for (String url: urlList
        ) {
            System.out.println("Testing URL: "+ url);
            this.mockMvc.perform(get(url)).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString(validResponse)));
            counter++;
        }

        System.out.println("Performed API requests for " + counter + " URL permutations");
    }

    /**
     * Tests single argument endpoint request where invalid base currency was provided.
     * Requested currency argument was checked in previous tests. It will not be included in the all following tests.
     * @throws Exception MockMVC Exception
     */
    @Test
    public void shouldReturnInvalidBaseCurrency() throws Exception {
        String validResponse = baseCurrencyInvalidJson;

        arguments.add(validApiKey);
        arguments.add(invalidBaseCurrency);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    /**
     * Tests all other arguments permutations endpoint request where invalid base currency was provided.
     * @throws Exception MockMVC Exception
     */
    @Test
    public void shouldReturnInvalidBaseCurrencyWithAllOtherArgsPermutations() throws Exception {
        String validResponse = baseCurrencyInvalidJson;

        List<String> urlList = multipleArgsCombinationsBuilder(validApiKey, invalidBaseCurrency, startDateArgs, finishDateArgs);
        int counter = 0;
        for (String url: urlList
        ) {

            System.out.println("Testing URL: "+ url);
            this.mockMvc.perform(get(url)).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString(validResponse)));
            counter++;
        }
        System.out.println("Performed API requests for " + counter + " URL permutations");
    }



    /**
     * Tests single argument endpoint request where invalid start date was provided.
     * @throws Exception MockMVC Exception
     */

    @Test
    public void shouldReturnInvalidStartDate() throws Exception {
        String validResponse = startDateInvalidJson;

        arguments.add(validApiKey);
        arguments.add(invalidStartDate);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    /**
     * Tests all other arguments permutations endpoint request where invalid start date was provided.
     * Base currency argument was checked in previous tests. It will not be included in the all following tests.
     * @throws Exception MockMVC Exception
     */

    @Test
    public void shouldReturnInvalidStartDateForArgsPermutations() throws Exception {
        String validResponse = startDateInvalidJson;

        List<String> urlList = multipleArgsCombinationsBuilder(validApiKey, invalidStartDate, finishDateArgs);
        int counter = 0;
        for (String url: urlList
        ) {

            System.out.println("Testing URL: "+ url);
            this.mockMvc.perform(get(url)).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString(validResponse)));
            counter++;
        }
        System.out.println("Performed API requests for " + counter + " URL permutations");
    }

    /**
     * Tests single argument endpoint request where invalid finish date was provided.
     * @throws Exception MockMVC Exception
     */
    @Test
    public void shouldReturnInvalidFinishDate() throws Exception {
        String validResponse = finishDateInvalidJson;

        arguments.add(validApiKey);
        arguments.add(invalidFinishDate);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    /**
     * Tests single argument endpoint request where no value finish date was provided.
     * @throws Exception MockMVC Exception
     */
    @Test
    public void shouldReturnInvalidFinishDate2() throws Exception {
        String validResponse = finishDateInvalidJson;

        arguments.add(validApiKey);
        arguments.add(emptyFinishDateArg);

        this.mockMvc.perform(get(urlBuilder())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(validResponse)));
    }

    /**
     * Returns list with all possible URL permutations for the requested arguments
     * @param baseAttribute1 unchangeable URL attribute
     * @param argList1 list of possible argument states (valid, invalid, empty, null)
     * @param argList2 list of possible argument states (valid, invalid, empty, null)
     * @param argList3 list of possible argument states (valid, invalid, empty, null)
     * @param argList4 list of possible argument states (valid, invalid, empty, null)
     * @return List of URL address permutations
     */
    public List<String> multipleArgsCombinationsBuilder(String baseAttribute1, String[] argList1, String[] argList2, String[] argList3, String[] argList4) {

        List<String> buildUrlList = new LinkedList<>();

        for (int argListOneIterator = 0; argListOneIterator < argList1.length; argListOneIterator++) {
            for (int argListTwoIterator = 0; argListTwoIterator < argList2.length; argListTwoIterator++) {
                for (int argListThreeIterator = 0; argListThreeIterator < argList3.length; argListThreeIterator++) {
                    for (int argListFourIterator = 0; argListFourIterator < argList4.length; argListFourIterator++) {
                        arguments.add(baseAttribute1);
                        arguments.add(argList1[argListOneIterator]);
                        arguments.add(argList2[argListTwoIterator]);
                        arguments.add(argList3[argListThreeIterator]);
                        arguments.add(argList4[argListFourIterator]);
                        buildUrlList.add(urlBuilder());
                        emptyArgsList();
                    }
                }
            }
        }
        return buildUrlList;
    }

    /**
     * Builds list with all possible URL permutations for the requested arguments
     * @param baseAttribute1 unchangeable URL attribute
     * @param baseAttribute2 unchangeable URL attribute
     * @param argList1 list of possible argument states (valid, invalid, empty, null)
     * @param argList2 list of possible argument states (valid, invalid, empty, null)
     * @param argList3 list of possible argument states (valid, invalid, empty, null)
     * @return List of URL address permutations
     */
    public List<String> multipleArgsCombinationsBuilder(String baseAttribute1, String baseAttribute2, String[] argList1, String[] argList2, String[] argList3) {

        List<String> buildUrlList = new LinkedList<>();

        for (int argListOneIterator = 0; argListOneIterator < argList1.length; argListOneIterator++) {
            for (int argListTwoIterator = 0; argListTwoIterator < argList2.length; argListTwoIterator++) {
                for (int argListThreeIterator = 0; argListThreeIterator < argList3.length; argListThreeIterator++) {
                    arguments.add(baseAttribute1);
                    arguments.add(baseAttribute2);
                    arguments.add(argList1[argListOneIterator]);
                    arguments.add(argList2[argListTwoIterator]);
                    arguments.add(argList3[argListThreeIterator]);
                    buildUrlList.add(urlBuilder());
                    emptyArgsList();
                }
            }
        }
        return buildUrlList;
    }

    /**
     * Builds list with all possible URL permutations for the requested arguments
     * @param baseAttribute1 unchangeable URL attribute
     * @param baseAttribute2 unchangeable URL attribute
     * @param argList1 list of possible argument states (valid, invalid, empty, null)
     * @param argList2 list of possible argument states (valid, invalid, empty, null)
     * @return List of URL address permutations
     */
    public List<String> multipleArgsCombinationsBuilder(String baseAttribute1, String baseAttribute2, String[] argList1, String[] argList2) {

        List<String> buildUrlList = new LinkedList<>();

        for (int argListOneIterator = 0; argListOneIterator < argList1.length; argListOneIterator++) {
            for (int argListTwoIterator = 0; argListTwoIterator < argList2.length; argListTwoIterator++) {
                    arguments.add(baseAttribute1);
                    arguments.add(baseAttribute2);
                    arguments.add(argList1[argListOneIterator]);
                    arguments.add(argList2[argListTwoIterator]);
                    buildUrlList.add(urlBuilder());
                    emptyArgsList();
            }
        }
        return buildUrlList;
    }

    /**
     * Builds list with all possible URL permutations for the requested arguments
     * @param baseAttribute1 unchangeable URL attribute
     * @param baseAttribute2 unchangeable URL attribute
     * @param argList1 list of possible argument states (valid, invalid, empty, null)
     * @return List of URL address permutations
     */
    public List<String> multipleArgsCombinationsBuilder(String baseAttribute1, String baseAttribute2, String[] argList1) {

        List<String> buildUrlList = new LinkedList<>();

        for (int argListOneIterator = 0; argListOneIterator < argList1.length; argListOneIterator++) {
                arguments.add(baseAttribute1);
                arguments.add(baseAttribute2);
                arguments.add(argList1[argListOneIterator]);
                buildUrlList.add(urlBuilder());
                emptyArgsList();
        }
        return buildUrlList;
    }


    public String urlBuilder() {
        StringBuilder returnedUrl = new StringBuilder(baseUrl);

        if (arguments.isEmpty()) {
            return returnedUrl.toString();
        }

        returnedUrl.append("?");

        for (int i = 0; i < arguments.size(); i++) {
            if (arguments.get(i) == null) {
                continue;
            }

            returnedUrl.append(arguments.get(i));

            if (i + 1 < arguments.size()) {
                returnedUrl.append("&");
            }
        }

        return returnedUrl.toString();
    }
}
