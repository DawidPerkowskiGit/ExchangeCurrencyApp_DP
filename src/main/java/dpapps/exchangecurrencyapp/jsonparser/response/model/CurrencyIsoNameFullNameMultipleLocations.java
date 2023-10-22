package dpapps.exchangecurrencyapp.jsonparser.response.model;

import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonConvertable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores currency iso name, full name and list of countries they can be used in.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyIsoNameFullNameMultipleLocations implements JsonConvertable {

    private String isoName;

    private String fullName;

    private List<String> locationList = new ArrayList<>();


}
