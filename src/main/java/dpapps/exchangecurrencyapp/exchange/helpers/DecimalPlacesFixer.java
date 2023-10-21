package dpapps.exchangecurrencyapp.exchange.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalPlacesFixer {

    /**
     * Modifies the number by leaving only requested number of decimal places. For example value=123.45678, decimalPlaces=3 will result in 123.456
     *
     * @param decimalPlaces number of decimal places left
     */
    public static double modifyNumberOfDecimalPlaces(double value, int decimalPlaces) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
