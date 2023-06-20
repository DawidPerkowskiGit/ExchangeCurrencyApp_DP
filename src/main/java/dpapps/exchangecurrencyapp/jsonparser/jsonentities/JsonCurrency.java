package dpapps.exchangecurrencyapp.jsonparser.jsonentities;

public class JsonCurrency {
    private String isoName;

    private String fullName;

    public JsonCurrency(String isoName, String fullName) {
        this.isoName = isoName;
        this.fullName = fullName;
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
}
