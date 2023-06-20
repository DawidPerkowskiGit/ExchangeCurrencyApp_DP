package dpapps.exchangecurrencyapp.jsonparser.jsonentities;

import dpapps.exchangecurrencyapp.exchange.entities.Location;

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

    public JsonLocation convertLocationToJsonLocation(Location location) {
        setName(location.getName());
        return this;
    }

    public Location convertJsonLocationToLocation() {
        Location location = new Location();
        location.setName(getName());
        return location;
    }
}
