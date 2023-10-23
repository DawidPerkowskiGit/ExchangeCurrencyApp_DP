package dpapps.exchangecurrencyapp.exchange.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonConvertable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JsonBuilderServiceImpl implements JsonBuilderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String buildJsonFromPojo(List<JsonConvertable> pojoList) {
        ObjectMapper objectMapper = new ObjectMapper();
        String exchangesToJson = "";

        if (pojoList.size() == 1) {
            try {
                exchangesToJson = objectMapper.writeValueAsString(pojoList.get(0));
            } catch (Exception e) {
                logger.error("Could not map object to JSON. Exception: " + e);
            }
        } else {
            try {
                exchangesToJson = objectMapper.writeValueAsString(pojoList);
            } catch (Exception e) {
                logger.error("Could not map object to JSON. Exception: " + e);
            }
        }

        return exchangesToJson;
    }
}
