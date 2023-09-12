package dpapps.exchangecurrencyapp.exchange.tools;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class StringToNumericConverter {
    public static Boolean isStringValidDouble(String input) {
        try {
            Double.parseDouble(input);
        }
        catch (Exception parsingException) {
            System.out.println("Cannot convert " + input + " to a Double");
            return false;
        }
        return true;
    }

    public static Double convertStringToDouble(String input) {
        return Double.parseDouble(input);
    }
}
