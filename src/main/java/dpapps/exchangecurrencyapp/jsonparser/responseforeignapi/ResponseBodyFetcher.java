package dpapps.exchangecurrencyapp.jsonparser.responseforeignapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * This module is responsible for sending API request to exchangeratesapi.io and fetching response body.
 */
@Service
public class ResponseBodyFetcher {


    public String retrieveApiResponse() {
        return retrieveApiResponse("");
    }


    /**
     * Retrieves exchangeratesapi.io response body
     *
     * @param url Optional url to the REST API service, default path is stored i environmental variable
     * @return JSON response
     */
    public String retrieveApiResponse(String url) {

        String apiUrl = url;

        if (apiUrl.equals("")) {
            try {
                apiUrl = System.getenv("API_IMPORT_CREDS_URL");
            } catch (Exception e) {
                System.out.printf("Could not retrieve environment variable API_IMPORT_CREDS_URL. Exception: " + e);
                return "";
            }
        }


        String apiResponseBody = "";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            apiResponseBody = response.body();
        } catch (Exception e) {
            System.out.printf("Could not process API request. Exception " + e);
            return "";
        }

        return apiResponseBody;
    }
}
