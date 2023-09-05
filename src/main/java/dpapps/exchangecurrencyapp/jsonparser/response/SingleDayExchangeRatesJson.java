package dpapps.exchangecurrencyapp.jsonparser.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonConvertable;

import java.time.LocalDate;
import java.util.Map;

/**
 * Holds Exchange model data, which is ready to be converted to JSON
 */
public class SingleDayExchangeRatesJson implements JsonConvertable {
    private boolean success = false;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String base;

    private Map<String, Double> rates;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public SingleDayExchangeRatesJson(boolean success, LocalDate date, Map<String, Double> rates, String base) {
        this.success = success;
        this.date = date;
        this.rates = rates;
        this.base = base;
    }

    public SingleDayExchangeRatesJson() {
    }
}
