package dpapps.exchangecurrencyapp.jsonparser.jsonentities;

import dpapps.exchangecurrencyapp.exchange.entities.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Stores and converts Location entity data to JSON format
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JsonLocation {

    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method converts Location object to JSON conversion ready object
     *
     * @param location Location object
     * @return JSON ready location object
     */
    public JsonLocation convertLocationToJsonLocation(Location location) {
        setName(location.getName());
        return this;
    }

}
