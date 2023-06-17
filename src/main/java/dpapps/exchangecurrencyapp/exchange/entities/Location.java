package dpapps.exchangecurrencyapp.exchange.entities;

import jakarta.persistence.*;

import java.util.List;

/**
 * Model of Location.
 *
 * Describes countries, lands, islands or other territories where currencies are used.
 */
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    private String locationName;

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
