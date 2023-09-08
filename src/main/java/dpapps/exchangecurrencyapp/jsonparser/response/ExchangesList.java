package dpapps.exchangecurrencyapp.jsonparser.response;

import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonConvertable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangesList implements JsonConvertable {
    List<JsonConvertable> exchangeList;
}
