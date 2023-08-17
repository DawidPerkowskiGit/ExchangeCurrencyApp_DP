package dpapps.exchangecurrencyapp.jsonparser.responsepojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Object holding currencies and countries they can be used in.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyNamesLocationObjectJsonReady implements JsonConvertable{

    private String isoName;

    private String fullName;

    private List<String> locationList = new ArrayList<>();


}
