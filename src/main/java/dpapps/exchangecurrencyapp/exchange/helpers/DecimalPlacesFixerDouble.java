package dpapps.exchangecurrencyapp.exchange.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalPlacesFixerDouble {
    public static double fix(double value, int decimalPlaces) {
        BigDecimal bd = BigDecimal.valueOf(1 / value);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
