package dpapps.exchangecurrencyapp.jsonparser.responsepojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyNamesLocationObject {
    private String iso_name;

    private String full_name;

    private List<String> locations;
}
