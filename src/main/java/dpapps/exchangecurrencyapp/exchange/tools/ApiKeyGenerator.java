package dpapps.exchangecurrencyapp.exchange.tools;

import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;

public class ApiKeyGenerator {

    public static String generateApiKey() {
        String apiKey = new RandomValueStringGenerator(64).generate();
        return apiKey;
    }


}
