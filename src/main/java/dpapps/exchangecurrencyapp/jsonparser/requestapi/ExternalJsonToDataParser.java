package dpapps.exchangecurrencyapp.jsonparser.requestapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import dpapps.exchangecurrencyapp.jsonparser.requestapi.model.ExternalDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Performs external service exchange rates JSON data deserialization. Data from a file or String variable can be deserialized.
 */
@Service
public class ExternalJsonToDataParser {

    private final boolean DEBUG = true;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Performs JSON file deserialization.
     * Reads JSON file, converts it to String and converts it to JAVA Object
     */
    public Optional<ExternalDataModel> parseJsonFromFile(String filePath) {

        ExternalDataModel externalDataModel = new ExternalDataModel();
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
     * Performs exchangeratesapi.io JSON body deserialization
     */
    public Optional<ExternalDataModel> jsonDeserialization(String jsonInString) {
        ExternalDataModel externalDataModel;
        try {
            ObjectMapper mapper = new ObjectMapper();
            externalDataModel = mapper.readValue(jsonInString, ExternalDataModel.class);
            if (DEBUG) {
                String pojoToString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(externalDataModel);
                logger.info("Object converted to String: " + pojoToString);
            }
        } catch (Exception e) {
            logger.error("Could not parse JSON body. Exception: " + e);
            return Optional.empty();
        }
        return Optional.ofNullable(externalDataModel);
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
