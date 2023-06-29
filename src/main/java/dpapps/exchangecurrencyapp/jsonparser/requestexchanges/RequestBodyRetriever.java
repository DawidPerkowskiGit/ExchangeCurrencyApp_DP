package dpapps.exchangecurrencyapp.jsonparser.requestexchanges;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class RequestBodyRetriever {

    /**
     * This module is responsible for sending API request to exchangeratesapi.io and extracting server's response body.
     */

    public String retrieveApiResponse() {

        String apiUrl = "";

        try {
            apiUrl = System.getenv("API_IMPORT_CREDS_URL");
        }
        catch (Exception e) {
            System.out.printf("Could not retrieve environment variable API_IMPORT_CREDS_URL. Exception: " + e);
            return "";
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
            return "";
        }

        return apiResponseBody;
    }
}