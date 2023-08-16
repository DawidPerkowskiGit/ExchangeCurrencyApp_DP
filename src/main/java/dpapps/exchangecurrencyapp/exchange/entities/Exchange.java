package dpapps.exchangecurrencyapp.exchange.entities;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

/**
 * Entity model of Exchange rates
 */
@Entity
@Table(name = "exchange")
public class Exchange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double value;

    @NonNull
    @ManyToOne
    private Currency currency;

    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "exchange_date")
    private LocalDate exchangeDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @NonNull
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @NonNull
    public LocalDate getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(LocalDate exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    @Override
    public String toString() {
        return getCurrency().getIsoName() + ", " + getExchangeDate().toString() + ", " + getValue();
    }

    public Exchange(Integer id, double value, Currency currency, LocalDate exchangeDate) {
        this.id = id;
        this.value = value;
        this.currency = currency;
        this.exchangeDate = exchangeDate;
    }

    public Exchange() {
    }

    public boolean doNonIdFieldsContainData() {
        if (getCurrency() == null) {
            return false;
        }
        if (getValue() == 0.0d) {
            return false;
        }
        if (getExchangeDate() == null) {
            return false;
        }
        return true;
    }
}