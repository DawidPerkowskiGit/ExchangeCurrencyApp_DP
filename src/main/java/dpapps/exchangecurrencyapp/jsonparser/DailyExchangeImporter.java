package dpapps.exchangecurrencyapp.jsonparser;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class DailyExchangeImporter {

    /**
     * Retrieve the latest currency exchange rates from
     */

    public String retireveApiResponse() {

        String apiUrl = "";

        try {
            apiUrl = System.getenv("API_IMPORT_CREDS_URL");
        }
        catch (Exception e) {
            System.out.printf("Could not retrieve environment variable API_IMPORT_CREDS_URL. Exception: " + e);
        }

        String apiResponseBody = "";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            apiResponseBody = response.body();
        }
        catch (Exception e) {
            System.out.printf("Could not process API request. Exception " + e);
        }

        return apiResponseBody;
    }
}
