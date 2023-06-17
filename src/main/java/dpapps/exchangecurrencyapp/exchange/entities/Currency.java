package dpapps.exchangecurrencyapp.exchange.entities;

import jakarta.persistence.*;

/**
 * Entity model of Currency.
 *
 * Describes its ISO Code name, full name.
 *
 * Relates to currency exchanges and locations where currencies are used
 *
 */
@Entity
@Table(name = "currency")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length=3, name="iso_name")
    private String isoName;

    @Column(length = 30, name="full_name")
    private String fullName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsoName() {
        return isoName;
    }

    public void setIsoName(String isoName) {
        this.isoName = isoName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return getIsoName();
    }

    public Currency() {}
    public Currency(Integer id, String currencyIsoName, String full_name) {
        this.id = id;
        this.isoName = currencyIsoName;
        this.fullName = full_name;
    }
}
