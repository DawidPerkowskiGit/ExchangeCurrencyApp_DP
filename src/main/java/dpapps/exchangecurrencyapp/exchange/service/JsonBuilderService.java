package dpapps.exchangecurrencyapp.exchange.service;

import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonConvertable;

import java.util.List;

public interface JsonBuilderService {

    public String buildJsonFromPojo(List<JsonConvertable> pojoList);

    public String buildJsonFromPojo(JsonConvertable pojoElement);
}
