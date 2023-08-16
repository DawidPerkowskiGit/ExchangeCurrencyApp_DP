package dpapps.exchangecurrencyapp.datetests;

import dpapps.exchangecurrencyapp.configuration.AppVariables;
import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.tools.DateRange;
import dpapps.exchangecurrencyapp.mockrepo.MockExchangeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DateTests {

     LocalDate beforeLeft   ;
     LocalDate left         ;
     LocalDate middle       ;
     LocalDate right        ;
     LocalDate afterRight   ;

     MockExchangeRepository exchangeRepository = new MockExchangeRepository();


    @BeforeEach
    public void setUp() {
        beforeLeft    = AppVariables.EXCHANGE_DATE_OLDEST.minusDays(1);
        left          = AppVariables.EXCHANGE_DATE_OLDEST;
        middle        = LocalDate.of(2010, 10, 10);
        right         = LocalDate.now();
        afterRight    = LocalDate.now().plusDays(1);
    }

    @BeforeEach
    public void setUpRepository() {
        LocalDate max = LocalDate.now();
        LocalDate current = AppVariables.EXCHANGE_DATE_OLDEST;

        while (current.isBefore(max) || current.isEqual(max)) {
            if (current.getDayOfWeek().toString().equals("SATURDAY") || current.getDayOfWeek().toString().equals("SUNDAY")) {
                //dont add weekend days
            }
            else {
                Exchange exchange = new Exchange();
                exchange.setExchangeDate(current);
                exchangeRepository.save(exchange);
            }
            current = current.plusDays(1);
        }
    }


    @Test
    public void assertThatSingleDateIsValid() {
        assertThat(DateRange.isDateInValidRange(beforeLeft)).isFalse();
        assertThat(DateRange.isDateInValidRange(left)).isTrue();
        assertThat(DateRange.isDateInValidRange(middle)).isTrue();
        assertThat(DateRange.isDateInValidRange(right)).isTrue();
        assertThat(DateRange.isDateInValidRange(afterRight)).isFalse();
    }

    @Test
    public void assertThatPairOfDatesIsValid() {
        assertThat(DateRange.isDateInValidRange(beforeLeft, beforeLeft)).isFalse();
        assertThat(DateRange.isDateInValidRange(beforeLeft, left)).isFalse();
        assertThat(DateRange.isDateInValidRange(beforeLeft, middle)).isFalse();
        assertThat(DateRange.isDateInValidRange(beforeLeft, right)).isFalse();
        assertThat(DateRange.isDateInValidRange(beforeLeft, afterRight)).isFalse();

        assertThat(DateRange.isDateInValidRange(left, beforeLeft)).isFalse();
        assertThat(DateRange.isDateInValidRange(left, left)).isTrue();
        assertThat(DateRange.isDateInValidRange(left, middle)).isTrue();
        assertThat(DateRange.isDateInValidRange(left, right)).isTrue();
        assertThat(DateRange.isDateInValidRange(left, afterRight)).isFalse();

        assertThat(DateRange.isDateInValidRange(middle, beforeLeft)).isFalse();
        assertThat(DateRange.isDateInValidRange(middle, left)).isTrue();
        assertThat(DateRange.isDateInValidRange(middle, middle)).isTrue();
        assertThat(DateRange.isDateInValidRange(middle, right)).isTrue();
        assertThat(DateRange.isDateInValidRange(middle, afterRight)).isFalse();

        assertThat(DateRange.isDateInValidRange(right, beforeLeft)).isFalse();
        assertThat(DateRange.isDateInValidRange(right, left)).isTrue();
        assertThat(DateRange.isDateInValidRange(right, middle)).isTrue();
        assertThat(DateRange.isDateInValidRange(right, right)).isTrue();
        assertThat(DateRange.isDateInValidRange(right, afterRight)).isFalse();

        assertThat(DateRange.isDateInValidRange(afterRight, beforeLeft)).isFalse();
        assertThat(DateRange.isDateInValidRange(afterRight, left)).isFalse();
        assertThat(DateRange.isDateInValidRange(afterRight, middle)).isFalse();
        assertThat(DateRange.isDateInValidRange(afterRight, right)).isFalse(); 
        assertThat(DateRange.isDateInValidRange(afterRight, afterRight)).isFalse();
    }

    @DirtiesContext
    @Test
    public void assertThatSingleDateIsInRangeAfterFixing() {
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(beforeLeft))).isTrue();
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(afterRight))).isTrue();
    }

    @DirtiesContext
    @Test
    public void assertThatPairsOfDatesAreInRangeAfterFixing() {
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(beforeLeft), DateRange.returnValidRange(beforeLeft))).isTrue();
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(beforeLeft), left)).isTrue();
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(beforeLeft), middle)).isTrue();
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(beforeLeft), right)).isTrue();
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(beforeLeft), DateRange.returnValidRange(afterRight))).isTrue();

        assertThat(DateRange.isDateInValidRange(left, DateRange.returnValidRange(beforeLeft))).isTrue();
        assertThat(DateRange.isDateInValidRange(left, DateRange.returnValidRange(afterRight))).isTrue();

        assertThat(DateRange.isDateInValidRange(middle, DateRange.returnValidRange(beforeLeft))).isTrue();
        assertThat(DateRange.isDateInValidRange(middle, DateRange.returnValidRange(afterRight))).isTrue();

        assertThat(DateRange.isDateInValidRange(right, DateRange.returnValidRange(beforeLeft))).isTrue();
        assertThat(DateRange.isDateInValidRange(right, DateRange.returnValidRange(afterRight))).isTrue();

        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(afterRight), DateRange.returnValidRange(beforeLeft))).isTrue();
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(afterRight), left)).isTrue();
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(afterRight), middle)).isTrue();
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(afterRight), right)).isTrue();
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(afterRight), DateRange.returnValidRange(afterRight))).isTrue();
    }

    @DirtiesContext
    @Test
    public void shouldReturnCorrectValuesOfFixedDate() {
        assertThat(DateRange.returnValidRange(beforeLeft).equals(left));
        assertThat(DateRange.returnValidRange(afterRight).equals(right));
    }

    @DirtiesContext
    @Test
    public void shouldReturnCorrectValuesOfFixedDates() {
        LocalDate[] shouldReturn = new LocalDate[2];

        shouldReturn[0] = left;
        shouldReturn[1] = left;
        LocalDate[] returnedPair = DateRange.returnValidRange(beforeLeft, beforeLeft);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = left;
        shouldReturn[1] = left;
        returnedPair = DateRange.returnValidRange(beforeLeft, left);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = left;
        shouldReturn[1] = middle;
        returnedPair = DateRange.returnValidRange(beforeLeft, middle);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = left;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(beforeLeft, right);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = left;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(beforeLeft, afterRight);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();


        shouldReturn[0] = left;
        shouldReturn[1] = left;
        returnedPair = DateRange.returnValidRange(left, beforeLeft);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = left;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(left, afterRight);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();


        shouldReturn[0] = left;
        shouldReturn[1] = middle;
        returnedPair = DateRange.returnValidRange(middle, beforeLeft);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = middle;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(middle, afterRight);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();


        shouldReturn[0] = left;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(right, beforeLeft);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = right;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(right, afterRight);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();


        shouldReturn[0] = left;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(afterRight, beforeLeft);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = left;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(afterRight, left);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = middle;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(afterRight, middle);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = right;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(afterRight, right);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = right;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(afterRight, afterRight);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();
    }

    @Test
    public void shouldReturnDatesExistingInDb() {
        LocalDate date1 = LocalDate.of(2023,06,11);
        LocalDate resultDate1 = LocalDate.of(2023,06,9);

        assertThat(resultDate1.isEqual(returnExchangeDateThatExistsInDb(date1))).isTrue();


        LocalDate date2 = LocalDate.of(2023,06,4);
        LocalDate resultDate2 = LocalDate.of(2023,06,2);

        assertThat(resultDate2.isEqual(returnExchangeDateThatExistsInDb(date2))).isTrue();


        assertThat(AppVariables.EXCHANGE_DATE_OLDEST.isEqual(returnExchangeDateThatExistsInDb(AppVariables.EXCHANGE_DATE_OLDEST).minusDays(1)));
        assertThat(AppVariables.EXCHANGE_DATE_OLDEST.isEqual(returnExchangeDateThatExistsInDb(AppVariables.EXCHANGE_DATE_OLDEST)));
        assertThat(AppVariables.EXCHANGE_DATE_OLDEST.plusDays(1).isEqual(returnExchangeDateThatExistsInDb(AppVariables.EXCHANGE_DATE_OLDEST).plusDays(1)));

    }

    public boolean compareTwoPairsOfDates(LocalDate[] pair1, LocalDate[] pair2) {
        if (pair1[0].isEqual(pair2[0]) || pair1[1].isEqual(pair2[1])) {
            return true;
        }
        return false;
    }

    public LocalDate returnExchangeDateThatExistsInDb(LocalDate date) {

        while (DateRange.isDateInValidRange(date)) {
            if (exchangeRepository.existsByExchangeDate(date) == false) {
                date = date.minusDays(1);
            }
            else {
                return date;
            }
        }
        return AppVariables.EXCHANGE_DATE_OLDEST;
    }
}
