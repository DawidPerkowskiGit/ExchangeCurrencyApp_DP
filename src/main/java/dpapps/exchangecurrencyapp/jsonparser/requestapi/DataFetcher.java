package dpapps.exchangecurrencyapp.jsonparser.requestapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * This module is responsible for sending API requestapi to exchangeratesapi.io and fetching requestapi body.
 */
@Service
public class DataFetcher {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public String retrieveApiResponse() {
        return retrieveApiResponse("");
    }


    /**
     * Retrieves exchangeratesapi.io requestapi body
     *
     * @param url Optional url to the REST API service, default path is stored i environmental variable
     * @return JSON requestapi
     */
    public String retrieveApiResponse(String url) {

        String apiUrl = url;

        if (apiUrl.equals("")) {
            try {
                apiUrl = System.getenv("API_IMPORT_CREDS_URL");
            } catch (Exception e) {
                logger.error("Could not retrieve environment variable API_IMPORT_CREDS_URL. Exception: " + e);
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
            logger.error("Could not process API requestapi. Exception " + e);
            return "";
        }

        return apiResponseBody;
    }
}
