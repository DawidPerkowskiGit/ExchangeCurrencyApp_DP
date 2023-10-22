package dpapps.exchangecurrencyapp.jsonparser.response.model;

import dpapps.exchangecurrencyapp.exchange.model.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Converts database Location entry to exchange loaction returned by REST API
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

    public JsonLocation convertLocationToJsonLocation(Location location) {
        setName(location.getName());
        return this;
    }

}
