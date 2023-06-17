package com.example.exchangecurrencyapp.exchange.entities;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<LocationCurrencyPair> location_currency;

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

    public List<LocationCurrencyPair> getLocation_currency() {
        return location_currency;
    }

    public void setLocation_currency(List<LocationCurrencyPair> location_currency) {
        this.location_currency = location_currency;
    }
}
