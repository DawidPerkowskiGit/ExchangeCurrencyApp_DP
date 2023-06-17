package com.example.exchangecurrencyapp.exchange.entities;

import jakarta.persistence.*;

/**
 * Model of currencies and locations where they are used.
 */
@Entity
public class LocationCurrencyPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationCurrencyPairId;

    @ManyToOne
    private Location locationId;

    @ManyToOne
    private Currency currencyId;

    public Long getLocationCurrencyPairId() {
        return locationCurrencyPairId;
    }

    public void setLocationCurrencyPairId(Long locationCurrencyPairId) {
        this.locationCurrencyPairId = locationCurrencyPairId;
    }

    public Location getLocationId() {
        return locationId;
    }

    public void setLocationId(Location locationId) {
        this.locationId = locationId;
    }

    public Currency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
    }
}
