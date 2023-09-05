package dpapps.exchangecurrencyapp.exchange.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonConvertable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JsonBuilderServiceImpl implements JsonBuilderService{
    /**
     * Returns list of JSON-parseable objects in JSON format
     *
     * @param pojoList Java List object containing Exchange or Currency data
     * @return Object list in JSON format
     */
    public String buildJsonFromPojo(List<JsonConvertable> pojoList) {
        ObjectMapper objectMapper = new ObjectMapper();
        String exchangesToJson = "";

        if (pojoList.size() == 1) {
            try {
                exchangesToJson = objectMapper.writeValueAsString(pojoList.get(0));
            } catch (Exception e) {
                System.out.println("Could not map object to JSON. Exception: " + e);
            }
        } else {
            try {
                exchangesToJson = objectMapper.writeValueAsString(pojoList);
            } catch (Exception e) {
                System.out.println("Could not map object to JSON. Exception: " + e);
            }
        }

        return exchangesToJson;
    }

    /**
     * Returns an Object in JSON format
     *
     * @param pojoElement Java object containing Exchange or Currency data
     * @return Requested data in JSON format
     */

    public String buildJsonFromPojo(JsonConvertable pojoElement) {

        List<JsonConvertable> list = new ArrayList<>();
        list.add(pojoElement);
        return buildJsonFromPojo(list);
    }
}
