package dpapps.exchangecurrencyapp.exchange.service;

import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonConvertable;

import java.util.List;

public interface JsonBuilderService {

    /**
     * Returns list of JSON-parseable objects in JSON format
     *
     * @param pojoList Java List object containing Exchange or Currency data
     * @return Object list in JSON format
     */
    public String buildJsonFromPojo(List<JsonConvertable> pojoList);

    /**
     * Returns an Object in JSON format
     *
     * @param pojoElement Java object containing Exchange or Currency data
     * @return Requested data in JSON format
     */
    public String buildJsonFromPojo(JsonConvertable pojoElement);
}
