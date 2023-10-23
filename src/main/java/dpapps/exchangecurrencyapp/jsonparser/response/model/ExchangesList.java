package dpapps.exchangecurrencyapp.jsonparser.response.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Stores exchanges from a single or multiple days. Object of this class is returned when /api/exchange endpoint is called.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangesList implements JsonConvertable {
    List<JsonConvertable> exchangeList;
}
