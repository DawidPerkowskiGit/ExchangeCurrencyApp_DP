package dpapps.exchangecurrencyapp.jsonparser.responseforeignapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * This class performs exchangeratesapi.io JSON response body deserialization.
 */
@Service
public class ResponseBodyJsonParser {

    private final boolean DEBUG = true;


    /**
     * Performs JSON file deserialization.
     * Reads JSON file, converts it to String and converts it to JAVA Object
     *
     * @param filePath filepath to the file
     * @return Java Object of exchangeratesapi.io exchange rates
     */
    public Optional<ResponseBodyObject> parseJsonFromFile(String filePath) {

        ResponseBodyObject responseBodyObject = new ResponseBodyObject();
        String jsonInString = "";

        try {
            jsonInString = readFileAsString(filePath);
        } catch (Exception e) {
            System.out.println("Could not load file. Exception " + e);
            return Optional.empty();
        }
        return jsonDeserialization(jsonInString);
    }

    /**
     * Performs JSON response body deserialziation
     *
     * @param jsonInString JSON stored in String type variable
     * @return Java Object of exchangeratesapi.io exchange rates
     */
    public Optional<ResponseBodyObject> jsonDeserialization(String jsonInString) {
        ResponseBodyObject responseBodyObject;
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseBodyObject = mapper.readValue(jsonInString, ResponseBodyObject.class);
            if (DEBUG) {
                String pojoToString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseBodyObject);
                System.out.println(pojoToString);
            }
        } catch (Exception e) {
            System.out.println("Could not parse JSON body. Exception: " + e);
            return Optional.empty();
        }
        return Optional.ofNullable(responseBodyObject);
    }


    /**
     * Reads file and returns its JSON content as String
     *
     * @param filePath Path to the file
     * @return String content of file
     * @throws Exception File reading exception
     */
    public String readFileAsString(String filePath) throws Exception {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }


}
