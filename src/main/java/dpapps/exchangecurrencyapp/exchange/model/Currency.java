package dpapps.exchangecurrencyapp.exchange.model;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

/**
 * Entity model of Currency.
 * <p>
 * Describes its ISO Code name, full name.
 * <p>
 * Relates to currency exchanges and locations where currencies are used
 */
@Entity
@Table(name = "currency")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(length = 3, name = "iso_name")
    private String isoName;

    @NonNull
    @Column(length = 30, name = "full_name")
    private String fullName;

    public Currency() {
    }

    public Currency(Integer id, String isoName, String fullName) {
        this.id = id;
        this.isoName = isoName;
        this.fullName = fullName;
    }

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
}
