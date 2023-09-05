package dpapps.exchangecurrencyapp.jsonparser.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Object of this class hold single database row containing currency iso name, currency full name and location.
 * List of these objects are going to be converted to List of locations and later to JSON format.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyNamesIsoFullLocationDatabaseEntry {
    private String iso_name;

    private String full_name;

    private String locations;

}
