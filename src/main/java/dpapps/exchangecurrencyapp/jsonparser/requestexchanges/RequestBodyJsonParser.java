package dpapps.exchangecurrencyapp.jsonparser.requestexchanges;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * This module is responsible for deserialization of response body.
 */
@Service
public class RequestBodyJsonParser {

    private final boolean DEBUG = true;


    public Optional<RequestBodyObject> parseJsonFromFile(String filePath) {

        RequestBodyObject requestBodyObject = new RequestBodyObject();
        String jsonInString = "";

        try {
            jsonInString = readFileAsString(filePath);
        } catch (Exception e) {
            System.out.println("Could not load file. Exception " + e);
            return Optional.empty();
        }
        return jsonDeserialization(jsonInString);
    }

    public Optional<RequestBodyObject> jsonDeserialization(String jsonInString) {
        RequestBodyObject requestBodyObject;
        try {
            ObjectMapper mapper = new ObjectMapper();
            requestBodyObject = mapper.readValue(jsonInString, RequestBodyObject.class);
            if (DEBUG) {
                String pojoToString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBodyObject);
                System.out.println(pojoToString);
            }
        } catch (Exception e) {
            System.out.println("Could not parse JSON body. Exception: " + e);
            return Optional.empty();
        }
        return Optional.ofNullable(requestBodyObject);
    }


    public String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }


}
