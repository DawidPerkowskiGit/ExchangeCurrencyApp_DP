package dpapps.exchangecurrencyapp.jsonparser.requestapi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

/**
 * The class represents data structure of exchangeratesapi.io JSON requestapi body.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExternalDataModel {

    private boolean success;

    private int timestamp;

    private String base;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Boolean historical;

    private Map<String, Double> rates;

    public boolean getSuccess() {
        return success;
    }

    public String getBase() {
        return base;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

    /**
     * Method checks if nullable fields contain data
     *
     * @return Result of checks
     */
    public boolean doAllNullableFieldsContainData() {
        if (getRates() == null) {
            return false;
        }
        if (getDate() == null) {
            return false;
        }
        if (getBase() == null) {
            return false;
        }
        return true;
    }

    public Boolean getHistorical() {
        return historical;
    }

    public void setHistorical(Boolean historical) {
        this.historical = historical;
    }
}
