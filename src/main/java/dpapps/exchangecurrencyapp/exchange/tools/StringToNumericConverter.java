package dpapps.exchangecurrencyapp.exchange.tools;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StringToNumericConverter {

    private static final Logger logger = LoggerFactory.getLogger(StringToNumericConverter.class);
    public static Boolean isStringValidDouble(String input) {
        try {
            Double.parseDouble(input);
        }
        catch (Exception parsingException) {
            logger.info("Cannot convert " + input + " to a Double");
            return false;
        }
        return true;
    }

    public static Double convertStringToDouble(String input) {
        return Double.parseDouble(input);
    }
}
