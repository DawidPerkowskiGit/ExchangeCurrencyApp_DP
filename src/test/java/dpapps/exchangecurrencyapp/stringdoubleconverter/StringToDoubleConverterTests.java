package dpapps.exchangecurrencyapp.stringdoubleconverter;

import dpapps.exchangecurrencyapp.exchange.helpers.StringIntConverter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StringToDoubleConverterTests {
    final List<String> invalidStringList = new LinkedList<>();

    final List<String> validStringList = new LinkedList<>();

    final List<Double> validDoubleList = new LinkedList<>();

    StringToDoubleConverterTests() {
        invalidStringList.add("A");
        invalidStringList.add("");
        invalidStringList.add(null);
        invalidStringList.add("3,5");
        invalidStringList.add("3.5A");

        validStringList.add("0");
        validStringList.add("-1.1");
        validStringList.add("-1.0");
        validStringList.add("-1");
        validStringList.add("1");
        validStringList.add("12.1234");


        validDoubleList.add(0d);
        validDoubleList.add(-1.1);
        validDoubleList.add(-1.0);
        validDoubleList.add(-1d);
        validDoubleList.add(1d);
        validDoubleList.add(12.1234);
    }

    @Test
    public void shouldReturnInvalidString() {
        for (String entry : invalidStringList
        ) {
            assertThat(StringIntConverter.isStringValidDouble(entry)).isFalse();
        }
    }

    @Test
    public void shouldReturnValidString() {
        for (String entry : validStringList
        ) {
            assertThat(StringIntConverter.isStringValidDouble(entry)).isTrue();
        }
    }

    @Test
    public void shouldReturnValidDouble() throws Exception {

        if (validDoubleList.size() == validDoubleList.size()) {
            for (int i = 0; i < validDoubleList.size(); i++) {
                assertThat(StringIntConverter.convertStringToDouble(validStringList.get(i))).isEqualTo(validDoubleList.get(i));
            }
        } else {
            throw new Exception("Invalid list size");
        }
    }
}
