package dpapps.exchangecurrencyapp.jsonparser.requestapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * This class performs exchangeratesapi.io JSON requestapi body deserialization.
 */
@Service
public class JsonToDataParser {

    private final boolean DEBUG = true;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Performs JSON file deserialization.
     * Reads JSON file, converts it to String and converts it to JAVA Object
     *
     * @param filePath filepath to the file
     * @return Java Object of exchangeratesapi.io exchange rates
     */
    public Optional<RequestDataModel> parseJsonFromFile(String filePath) {

        RequestDataModel requestDataModel = new RequestDataModel();
        String jsonInString = "";

        try {
            jsonInString = readFileAsString(filePath);
        } catch (Exception e) {
            logger.error("Could not load file. Exception " + e);
            return Optional.empty();
        }
        return jsonDeserialization(jsonInString);
    }

    /**
     * Performs JSON requestapi body deserialziation
     *
     * @param jsonInString JSON stored in String type variable
     * @return Java Object of exchangeratesapi.io exchange rates
     */
    public Optional<RequestDataModel> jsonDeserialization(String jsonInString) {
        RequestDataModel requestDataModel;
        try {
            ObjectMapper mapper = new ObjectMapper();
            requestDataModel = mapper.readValue(jsonInString, RequestDataModel.class);
            if (DEBUG) {
                String pojoToString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDataModel);
                logger.info("Object converted to String: " + pojoToString);
            }
        } catch (Exception e) {
            logger.error("Could not parse JSON body. Exception: " + e);
            return Optional.empty();
        }
        return Optional.ofNullable(requestDataModel);
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
