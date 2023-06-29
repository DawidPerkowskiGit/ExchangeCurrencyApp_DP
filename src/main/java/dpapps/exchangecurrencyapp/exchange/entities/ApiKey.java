package dpapps.exchangecurrencyapp.exchange.entities;

import dpapps.exchangecurrencyapp.configuration.AppVariables;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;


@Entity
public class ApiKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="value")
    private String value;

    @Column(name="active")
    private boolean active;

    @Column(name = "current_requests_count")
    private int currentRequestsCount = 0;

    @Column
    private int maximumRequestsCount = AppVariables.GLOBAL_LIMIT_OF_DAILY_USAGES;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getCurrentRequestsCount() {
        return currentRequestsCount;
    }

    public void setCurrentRequestsCount(int currentRequestsCount) {
        this.currentRequestsCount = currentRequestsCount;
    }

    public void resetNumberOfUsages() {
        this.currentRequestsCount = 0;
    }

    public int getMaximumRequestsCount() {
        return maximumRequestsCount;
    }

    public void setMaximumRequestsCount(int maximumRequestsCount) {
        this.maximumRequestsCount = maximumRequestsCount;
    }

    public void increaseNumberOfUsages() {
        if (currentRequestsCount < maximumRequestsCount) {
            currentRequestsCount++;
        }
    }

    public boolean isRequestLimitIsReached() {
        if (currentRequestsCount == maximumRequestsCount) {
            return true;
        }
        return false;
    }

    public boolean verifyIfCanPerformRequest() {
        if (isRequestLimitIsReached()) {
            return false;
        }
        if (isActive() == false) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiKey apiKey)) return false;
        return active == apiKey.active && currentRequestsCount == apiKey.currentRequestsCount && maximumRequestsCount == apiKey.maximumRequestsCount && id.equals(apiKey.id) && value.equals(apiKey.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, active, currentRequestsCount, maximumRequestsCount);
    }
}
