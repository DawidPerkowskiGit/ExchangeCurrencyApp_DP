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

     LocalDate latestDate;

     MockExchangeRepository exchangeRepository = new MockExchangeRepository();


    @BeforeEach
    public void setUp() {
        beforeLeft    = AppVariables.EXCHANGE_DATE_OLDEST.minusDays(1);
        left          = AppVariables.EXCHANGE_DATE_OLDEST;
        middle        = LocalDate.of(2010, 10, 10);
        right         = LocalDate.now();
        afterRight    = LocalDate.now().plusDays(1);
        latestDate    = LocalDate.now();
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
        assertThat(DateRange.isDateInValidRange(beforeLeft, latestDate)).isFalse();
        assertThat(DateRange.isDateInValidRange(left, latestDate)).isTrue();
        assertThat(DateRange.isDateInValidRange(middle, latestDate)).isTrue();
        assertThat(DateRange.isDateInValidRange(right, latestDate)).isTrue();
        assertThat(DateRange.isDateInValidRange(afterRight, latestDate)).isFalse();
    }

    @Test
    public void assertThatPairOfDatesIsValid() {
        assertThat(DateRange.isDateInValidRange(beforeLeft, beforeLeft, latestDate)).isFalse();
        assertThat(DateRange.isDateInValidRange(beforeLeft, left, latestDate)).isFalse();
        assertThat(DateRange.isDateInValidRange(beforeLeft, middle, latestDate)).isFalse();
        assertThat(DateRange.isDateInValidRange(beforeLeft, right, latestDate)).isFalse();
        assertThat(DateRange.isDateInValidRange(beforeLeft, afterRight, latestDate)).isFalse();

        assertThat(DateRange.isDateInValidRange(left, beforeLeft, latestDate)).isFalse();
        assertThat(DateRange.isDateInValidRange(left, left, latestDate)).isTrue();
        assertThat(DateRange.isDateInValidRange(left, middle, latestDate)).isTrue();
        assertThat(DateRange.isDateInValidRange(left, right, latestDate)).isTrue();
        assertThat(DateRange.isDateInValidRange(left, afterRight, latestDate)).isFalse();

        assertThat(DateRange.isDateInValidRange(middle, beforeLeft, latestDate)).isFalse();
        assertThat(DateRange.isDateInValidRange(middle, left, latestDate)).isTrue();
        assertThat(DateRange.isDateInValidRange(middle, middle, latestDate)).isTrue();
        assertThat(DateRange.isDateInValidRange(middle, right, latestDate)).isTrue();
        assertThat(DateRange.isDateInValidRange(middle, afterRight, latestDate)).isFalse();

        assertThat(DateRange.isDateInValidRange(right, beforeLeft, latestDate)).isFalse();
        assertThat(DateRange.isDateInValidRange(right, left, latestDate)).isTrue();
        assertThat(DateRange.isDateInValidRange(right, middle, latestDate)).isTrue();
        assertThat(DateRange.isDateInValidRange(right, right, latestDate)).isTrue();
        assertThat(DateRange.isDateInValidRange(right, afterRight, latestDate)).isFalse();

        assertThat(DateRange.isDateInValidRange(afterRight, beforeLeft, latestDate)).isFalse();
        assertThat(DateRange.isDateInValidRange(afterRight, left, latestDate)).isFalse();
        assertThat(DateRange.isDateInValidRange(afterRight, middle, latestDate)).isFalse();
        assertThat(DateRange.isDateInValidRange(afterRight, right, latestDate)).isFalse();
        assertThat(DateRange.isDateInValidRange(afterRight, afterRight, latestDate)).isFalse();
    }

    @DirtiesContext
    @Test
    public void assertThatSingleDateIsInRangeAfterFixing() {
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(beforeLeft, latestDate) , latestDate)).isTrue();
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(afterRight, latestDate) , latestDate)).isTrue();
    }

    @DirtiesContext
    @Test
    public void assertThatPairsOfDatesAreInRangeAfterFixing() {
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(beforeLeft, latestDate), DateRange.returnValidRange(beforeLeft, latestDate))).isTrue();
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(beforeLeft, latestDate), left, latestDate)).isTrue();
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(beforeLeft, latestDate), middle, latestDate)).isTrue();
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(beforeLeft, latestDate), right)).isTrue();
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(beforeLeft, latestDate), DateRange.returnValidRange(afterRight, latestDate))).isTrue();

        assertThat(DateRange.isDateInValidRange(left, DateRange.returnValidRange(beforeLeft, latestDate))).isTrue();
        assertThat(DateRange.isDateInValidRange(left, DateRange.returnValidRange(afterRight, latestDate))).isTrue();

        assertThat(DateRange.isDateInValidRange(middle, DateRange.returnValidRange(beforeLeft, latestDate))).isTrue();
        assertThat(DateRange.isDateInValidRange(middle, DateRange.returnValidRange(afterRight, latestDate))).isTrue();

        assertThat(DateRange.isDateInValidRange(right, DateRange.returnValidRange(beforeLeft, latestDate))).isTrue();
        assertThat(DateRange.isDateInValidRange(right, DateRange.returnValidRange(afterRight, latestDate))).isTrue();

        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(afterRight, latestDate), DateRange.returnValidRange(beforeLeft, latestDate))).isTrue();
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(afterRight, latestDate), left, latestDate)).isTrue();
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(afterRight, latestDate), middle, latestDate)).isTrue();
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(afterRight, latestDate), right, latestDate)).isTrue();
        assertThat(DateRange.isDateInValidRange(DateRange.returnValidRange(afterRight, latestDate), DateRange.returnValidRange(afterRight, latestDate))).isTrue();
    }

    @DirtiesContext
    @Test
    public void shouldReturnCorrectValuesOfFixedDate() {
        assertThat(DateRange.returnValidRange(beforeLeft, latestDate).equals(left));
        assertThat(DateRange.returnValidRange(afterRight, latestDate).equals(right));
    }

    @DirtiesContext
    @Test
    public void shouldReturnCorrectValuesOfFixedDates() {
        LocalDate[] shouldReturn = new LocalDate[2];

        shouldReturn[0] = left;
        shouldReturn[1] = left;
        LocalDate[] returnedPair = DateRange.returnValidRange(beforeLeft, beforeLeft, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = left;
        shouldReturn[1] = left;
        returnedPair = DateRange.returnValidRange(beforeLeft, left, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = left;
        shouldReturn[1] = middle;
        returnedPair = DateRange.returnValidRange(beforeLeft, middle, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = left;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(beforeLeft, right, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = left;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(beforeLeft, afterRight, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();


        shouldReturn[0] = left;
        shouldReturn[1] = left;
        returnedPair = DateRange.returnValidRange(left, beforeLeft, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = left;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(left, afterRight, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();


        shouldReturn[0] = left;
        shouldReturn[1] = middle;
        returnedPair = DateRange.returnValidRange(middle, beforeLeft, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = middle;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(middle, afterRight, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();


        shouldReturn[0] = left;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(right, beforeLeft, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = right;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(right, afterRight, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();


        shouldReturn[0] = left;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(afterRight, beforeLeft, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = left;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(afterRight, left, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = middle;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(afterRight, middle, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = right;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(afterRight, right, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = right;
        shouldReturn[1] = right;
        returnedPair = DateRange.returnValidRange(afterRight, afterRight, latestDate);
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

        while (DateRange.isDateInValidRange(date, latestDate)) {
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
