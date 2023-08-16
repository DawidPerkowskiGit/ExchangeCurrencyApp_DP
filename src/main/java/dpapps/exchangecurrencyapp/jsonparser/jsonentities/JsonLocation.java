package dpapps.exchangecurrencyapp.jsonparser.jsonentities;

import dpapps.exchangecurrencyapp.exchange.entities.Location;

/**
 * Location Object conversion to JSON ready object
 */
public class JsonLocation {

    private String name;

    public JsonLocation(String name) {
        this.name = name;
    }

    public JsonLocation() {
    }

    public JsonLocation(Location location) {
        convertLocationToJsonLocation(location);
    }

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
