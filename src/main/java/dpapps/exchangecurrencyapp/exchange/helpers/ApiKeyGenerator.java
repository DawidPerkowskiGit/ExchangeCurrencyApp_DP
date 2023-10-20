package dpapps.exchangecurrencyapp.exchange.helpers;

import dpapps.exchangecurrencyapp.configuration.AppVariables;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;

/**
 * API keys generator
 */
public class ApiKeyGenerator {

    /**
     * Generates new API key
     *
     * @return API key as a String
     */
    public static String generateApiKey() {
        String apiKey = new RandomValueStringGenerator(AppVariables.API_KEY_GENERATED_LENGTH).generate();
        return apiKey;
    }


}