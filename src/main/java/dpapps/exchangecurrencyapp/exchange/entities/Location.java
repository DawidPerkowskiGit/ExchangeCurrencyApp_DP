package dpapps.exchangecurrencyapp.exchange.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

/**
 * Model of Location.
 * <p>
 * Describes countries, lands, islands or other territories where currencies are used.
 */
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(length = 50)
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Location() {
    }
}
