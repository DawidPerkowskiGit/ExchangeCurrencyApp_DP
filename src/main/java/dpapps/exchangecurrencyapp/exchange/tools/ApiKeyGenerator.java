package dpapps.exchangecurrencyapp.exchange.tools;

import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;

/**
 * API keys generator
 */
public class ApiKeyGenerator {

    /**
     * Generates new API key
     *
     * @return API key
     */
    public static String generateApiKey() {
        String apiKey = new RandomValueStringGenerator(64).generate();
        return apiKey;
    }


}
