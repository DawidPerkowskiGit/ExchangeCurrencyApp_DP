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
    String buildJsonFromPojo(List<JsonConvertable> pojoList);

}
