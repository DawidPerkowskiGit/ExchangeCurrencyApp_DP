package dpapps.exchangecurrencyapp.locationtests;

import dpapps.exchangecurrencyapp.exchange.model.Location;
import dpapps.exchangecurrencyapp.jsonparser.response.model.JsonLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class LocationJsonTests {

    @Autowired
    private JacksonTester<JsonLocation> jacksonJsonLocation;

    @Autowired
    private JacksonTester<List<JsonLocation>> jacksonJsonLocationList;

    private List<JsonLocation> jsonLocationList = new ArrayList<>();

    @Autowired
    private JacksonTester<LocationJsonTests> jacksonLocation;

    @Autowired
    private JacksonTester<List<Location>> jacksonLocationList;

    private List<Location> locationList = new ArrayList<>();

    @BeforeEach
    void setUp() {

        locationList.add(new Location(1, "Germany"));
        locationList.add(new Location(2, "United States"));
        locationList.add(new Location(3, "Japan"));

        jsonLocationList.add(new JsonLocation().convertLocationToJsonLocation(locationList.get(0)));
        jsonLocationList.add(new JsonLocation().convertLocationToJsonLocation(locationList.get(1)));
        jsonLocationList.add(new JsonLocation().convertLocationToJsonLocation(locationList.get(2)));

    }

    @Test
    public void locationSerializationTest() throws IOException {
        JsonLocation location = jsonLocationList.get(0);
        assertThat(jacksonJsonLocation.write(location)).isStrictlyEqualToJson("locationSingle.json");
        assertThat(jacksonJsonLocation.write(location)).hasJsonPathValue("@.name");
        assertThat(jacksonJsonLocation.write(location)).extractingJsonPathValue("@.name").isEqualTo("Germany");
    }
    @Test
    public void locationDeserializationTest() throws IOException {
        String expected = """
                {
                  "name": "Germany"
                }
                """;
        JsonLocation location = new JsonLocation("Germany");
        assertThat(jacksonJsonLocation.write(location)).isEqualToJson(expected);
    }

    @Test
    void currencyListSerializationTest() throws IOException {
        assertThat(jacksonJsonLocationList.write(jsonLocationList)).isStrictlyEqualToJson("locationList.json");
    }

    @Test
    void currencyListDeserializationTest() throws IOException {
        String expected = """
                [
                  {"name": "Germany"},
                  {"name": "United States"},
                  {"name": "Japan"}
                ]
                """;
        assertThat(jacksonJsonLocationList.write(jsonLocationList)).isEqualToJson(expected);
    }
}
