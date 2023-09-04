package dpapps.exchangecurrencyapp.exchange.model;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

/**
 * Model of currencies and locations where they are used.
 */
@Entity
public class LocationCurrencyPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @ManyToOne
    private Location location;

    @NonNull
    @ManyToOne
    private Currency currency;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
