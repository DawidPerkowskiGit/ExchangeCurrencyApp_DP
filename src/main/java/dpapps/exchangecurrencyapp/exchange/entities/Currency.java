package dpapps.exchangecurrencyapp.exchange.entities;

import jakarta.persistence.*;

import java.util.List;

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
    private Long currencyId;

    @Column(length=3)
    private String currencyIsoName;

    private String full_name;

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyIsoName() {
        return currencyIsoName;
    }

    public void setCurrencyIsoName(String currencyIsoName) {
        this.currencyIsoName = currencyIsoName;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    @Override
    public String toString() {
        return getCurrencyIsoName();
    }

    public Currency() {}
    public Currency(Long currencyId, String currencyIsoName, String full_name) {
        this.currencyId = currencyId;
        this.currencyIsoName = currencyIsoName;
        this.full_name = full_name;
    }
}
