package dpapps.exchangecurrencyapp.exchange.entities;

import jakarta.persistence.*;

/**
 * Model of Location.
 *
 * Describes countries, lands, islands or other territories where currencies are used.
 */
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
}
