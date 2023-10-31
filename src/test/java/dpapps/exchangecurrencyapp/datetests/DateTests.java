package dpapps.exchangecurrencyapp.datetests;

import dpapps.exchangecurrencyapp.configuration.AppConstants;
import dpapps.exchangecurrencyapp.exchange.helpers.DateRangeValidator;
import dpapps.exchangecurrencyapp.exchange.model.Exchange;
import dpapps.exchangecurrencyapp.mockrepo.MockExchangeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DateTests {

    LocalDate beforeLeft;
    LocalDate left;
    LocalDate middle;
    LocalDate right;
    LocalDate afterRight;

    LocalDate latestDate;

    MockExchangeRepository exchangeRepository = new MockExchangeRepository();


    @BeforeEach
    public void setUp() {
        beforeLeft = AppConstants.EXCHANGE_DATE_OLDEST.minusDays(1);
        left = AppConstants.EXCHANGE_DATE_OLDEST;
        middle = LocalDate.of(2010, 10, 10);
        right = LocalDate.now();
        afterRight = LocalDate.now().plusDays(1);
        latestDate = LocalDate.now();
    }

    @BeforeEach
    public void setUpRepository() {
        LocalDate max = LocalDate.now();
        LocalDate current = AppConstants.EXCHANGE_DATE_OLDEST;


        while (current.isBefore(max) || current.isEqual(max)) {
            if (current.getDayOfWeek().toString().equals("SATURDAY") || current.getDayOfWeek().toString().equals("SUNDAY")) {
                //dont add weekend days
            } else {
                Exchange exchange = new Exchange();
                exchange.setExchangeDate(current);
                exchangeRepository.save(exchange);
            }
            current = current.plusDays(1);
        }
    }


    @Test
    public void assertThatSingleDateIsValid() {
        assertThat(DateRangeValidator.isDateInValidRange(beforeLeft, latestDate)).isFalse();
        assertThat(DateRangeValidator.isDateInValidRange(left, latestDate)).isTrue();
        assertThat(DateRangeValidator.isDateInValidRange(middle, latestDate)).isTrue();
        assertThat(DateRangeValidator.isDateInValidRange(right, latestDate)).isTrue();
        assertThat(DateRangeValidator.isDateInValidRange(afterRight, latestDate)).isFalse();
    }

    @Test
    public void assertThatPairOfDatesAreValid() {
        assertThat(DateRangeValidator.areDatesInValidRange(beforeLeft, beforeLeft, latestDate)).isFalse();
        assertThat(DateRangeValidator.areDatesInValidRange(beforeLeft, left, latestDate)).isFalse();
        assertThat(DateRangeValidator.areDatesInValidRange(beforeLeft, middle, latestDate)).isFalse();
        assertThat(DateRangeValidator.areDatesInValidRange(beforeLeft, right, latestDate)).isFalse();
        assertThat(DateRangeValidator.areDatesInValidRange(beforeLeft, afterRight, latestDate)).isFalse();

        assertThat(DateRangeValidator.areDatesInValidRange(left, beforeLeft, latestDate)).isFalse();
        assertThat(DateRangeValidator.areDatesInValidRange(left, left, latestDate)).isTrue();
        assertThat(DateRangeValidator.areDatesInValidRange(left, middle, latestDate)).isTrue();
        assertThat(DateRangeValidator.areDatesInValidRange(left, right, latestDate)).isTrue();
        assertThat(DateRangeValidator.areDatesInValidRange(left, afterRight, latestDate)).isFalse();

        assertThat(DateRangeValidator.areDatesInValidRange(middle, beforeLeft, latestDate)).isFalse();
        assertThat(DateRangeValidator.areDatesInValidRange(middle, left, latestDate)).isFalse();
        assertThat(DateRangeValidator.areDatesInValidRange(middle, middle, latestDate)).isTrue();
        assertThat(DateRangeValidator.areDatesInValidRange(middle, right, latestDate)).isTrue();
        assertThat(DateRangeValidator.areDatesInValidRange(middle, afterRight, latestDate)).isFalse();

        assertThat(DateRangeValidator.areDatesInValidRange(right, beforeLeft, latestDate)).isFalse();
        assertThat(DateRangeValidator.areDatesInValidRange(right, left, latestDate)).isFalse();
        assertThat(DateRangeValidator.areDatesInValidRange(right, middle, latestDate)).isFalse();
        assertThat(DateRangeValidator.areDatesInValidRange(right, right, latestDate)).isTrue();
        assertThat(DateRangeValidator.areDatesInValidRange(right, afterRight, latestDate)).isFalse();

        assertThat(DateRangeValidator.areDatesInValidRange(afterRight, beforeLeft, latestDate)).isFalse();
        assertThat(DateRangeValidator.areDatesInValidRange(afterRight, left, latestDate)).isFalse();
        assertThat(DateRangeValidator.areDatesInValidRange(afterRight, middle, latestDate)).isFalse();
        assertThat(DateRangeValidator.areDatesInValidRange(afterRight, right, latestDate)).isFalse();
        assertThat(DateRangeValidator.areDatesInValidRange(afterRight, afterRight, latestDate)).isFalse();
    }

    @DirtiesContext
    @Test
    public void assertThatSingleDateIsInRangeAfterFixing() {
        assertThat(DateRangeValidator.isDateInValidRange(DateRangeValidator.returnValidRange(beforeLeft, latestDate), latestDate)).isTrue();
        assertThat(DateRangeValidator.isDateInValidRange(DateRangeValidator.returnValidRange(afterRight, latestDate), latestDate)).isTrue();
    }

    @DirtiesContext
    @Test
    public void assertThatPairsOfDatesAreInRangeAfterFixing() {
        assertThat(
                DateRangeValidator.areDatesInValidRange(
                        DateRangeValidator.returnValidRange(beforeLeft, latestDate),
                        DateRangeValidator.returnValidRange(beforeLeft, latestDate),
                        latestDate
                )
        ).isTrue();

        assertThat(
                DateRangeValidator.areDatesInValidRange(
                        DateRangeValidator.returnValidRange(beforeLeft, latestDate),
                        left,
                        latestDate
                )
        ).isTrue();

        assertThat(
                DateRangeValidator.areDatesInValidRange(
                        DateRangeValidator.returnValidRange(beforeLeft, latestDate),
                        middle,
                        latestDate
                )
        ).isTrue();

        assertThat(
                DateRangeValidator.areDatesInValidRange(
                        DateRangeValidator.returnValidRange(beforeLeft, latestDate),
                        right,
                        latestDate
                )
        ).isTrue();

        assertThat(
                DateRangeValidator.areDatesInValidRange(
                        DateRangeValidator.returnValidRange(beforeLeft, latestDate),
                        DateRangeValidator.returnValidRange(afterRight, latestDate),
                        latestDate
                )
        ).isTrue();

        assertThat(
                DateRangeValidator.areDatesInValidRange(
                        left,
                        DateRangeValidator.returnValidRange(beforeLeft, latestDate),
                        latestDate
                )
        ).isTrue();

        assertThat(
                DateRangeValidator.areDatesInValidRange(
                        left,
                        DateRangeValidator.returnValidRange(afterRight, latestDate),
                        latestDate
                )
        ).isTrue();

        assertThat(
                DateRangeValidator.areDatesInValidRange(
                        middle,
                        DateRangeValidator.returnValidRange(beforeLeft, latestDate),
                        latestDate
                )
        ).isFalse();

        assertThat(
                DateRangeValidator.areDatesInValidRange(
                        middle,
                        DateRangeValidator.returnValidRange(afterRight, latestDate),
                        latestDate
                )
        ).isTrue();

        assertThat(
                DateRangeValidator.areDatesInValidRange(
                        right,
                        DateRangeValidator.returnValidRange(beforeLeft, latestDate),
                        latestDate
                )
        ).isFalse();

        assertThat(
                DateRangeValidator.areDatesInValidRange(right,
                        DateRangeValidator.returnValidRange(afterRight, latestDate),
                        latestDate
                )
        ).isTrue();

        assertThat(
                DateRangeValidator.areDatesInValidRange(
                        DateRangeValidator.returnValidRange(afterRight, latestDate),
                        DateRangeValidator.returnValidRange(beforeLeft, latestDate),
                        latestDate
                )
        ).isFalse();

        assertThat(
                DateRangeValidator.areDatesInValidRange(
                        DateRangeValidator.returnValidRange(afterRight, latestDate),
                        left,
                        latestDate
                )
        ).isFalse();

        assertThat(
                DateRangeValidator.areDatesInValidRange(
                        DateRangeValidator.returnValidRange(afterRight, latestDate),
                        middle,
                        latestDate
                )
        ).isFalse();

        assertThat(
                DateRangeValidator.areDatesInValidRange(
                        DateRangeValidator.returnValidRange(afterRight, latestDate),
                        right,
                        latestDate
                )
        ).isTrue();

        assertThat(
                DateRangeValidator.areDatesInValidRange(
                        DateRangeValidator.returnValidRange(afterRight, latestDate),
                        DateRangeValidator.returnValidRange(afterRight, latestDate),
                        latestDate
                )
        ).isTrue();
    }

    @DirtiesContext
    @Test
    public void shouldReturnCorrectValuesOfFixedDate() {
        assertThat(DateRangeValidator.returnValidRange(beforeLeft, latestDate).equals(left)).isTrue();
        assertThat(DateRangeValidator.returnValidRange(afterRight, latestDate).equals(right)).isTrue();
    }

    @DirtiesContext
    @Test
    public void shouldReturnCorrectValuesOfFixedDates() {
        LocalDate[] shouldReturn = new LocalDate[2];

        shouldReturn[0] = left;
        shouldReturn[1] = left;
        LocalDate[] returnedPair = DateRangeValidator.returnValidRange(beforeLeft, beforeLeft, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = left;
        shouldReturn[1] = left;
        returnedPair = DateRangeValidator.returnValidRange(beforeLeft, left, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = left;
        shouldReturn[1] = middle;
        returnedPair = DateRangeValidator.returnValidRange(beforeLeft, middle, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = left;
        shouldReturn[1] = right;
        returnedPair = DateRangeValidator.returnValidRange(beforeLeft, right, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = left;
        shouldReturn[1] = right;
        returnedPair = DateRangeValidator.returnValidRange(beforeLeft, afterRight, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();


        shouldReturn[0] = left;
        shouldReturn[1] = left;
        returnedPair = DateRangeValidator.returnValidRange(left, beforeLeft, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = left;
        shouldReturn[1] = right;
        returnedPair = DateRangeValidator.returnValidRange(left, afterRight, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();


        shouldReturn[0] = left;
        shouldReturn[1] = middle;
        returnedPair = DateRangeValidator.returnValidRange(middle, beforeLeft, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = middle;
        shouldReturn[1] = right;
        returnedPair = DateRangeValidator.returnValidRange(middle, afterRight, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();


        shouldReturn[0] = left;
        shouldReturn[1] = right;
        returnedPair = DateRangeValidator.returnValidRange(right, beforeLeft, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = right;
        shouldReturn[1] = right;
        returnedPair = DateRangeValidator.returnValidRange(right, afterRight, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();


        shouldReturn[0] = left;
        shouldReturn[1] = right;
        returnedPair = DateRangeValidator.returnValidRange(afterRight, beforeLeft, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = left;
        shouldReturn[1] = right;
        returnedPair = DateRangeValidator.returnValidRange(afterRight, left, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = middle;
        shouldReturn[1] = right;
        returnedPair = DateRangeValidator.returnValidRange(afterRight, middle, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = right;
        shouldReturn[1] = right;
        returnedPair = DateRangeValidator.returnValidRange(afterRight, right, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();

        shouldReturn[0] = right;
        shouldReturn[1] = right;
        returnedPair = DateRangeValidator.returnValidRange(afterRight, afterRight, latestDate);
        assertThat(compareTwoPairsOfDates(shouldReturn, returnedPair)).isTrue();
    }

    @Test
    public void shouldReturnDatesExistingInDb() {
        LocalDate date1 = LocalDate.of(2023, 6, 11);
        LocalDate resultDate1 = LocalDate.of(2023, 6, 11);

        assertThat(resultDate1.isEqual(DateRangeValidator.returnValidRange(date1, latestDate))).isTrue();


        LocalDate date2 = LocalDate.of(2023, 6, 4);
        LocalDate resultDate2 = LocalDate.of(2023, 6, 4);

        assertThat(resultDate2.isEqual(DateRangeValidator.returnValidRange(date2, latestDate))).isTrue();

        assertThat(AppConstants.EXCHANGE_DATE_OLDEST.isEqual(DateRangeValidator.returnValidRange((AppConstants.EXCHANGE_DATE_OLDEST).minusDays(1), latestDate))).isTrue();
        assertThat(AppConstants.EXCHANGE_DATE_OLDEST.isEqual(DateRangeValidator.returnValidRange(AppConstants.EXCHANGE_DATE_OLDEST, latestDate))).isTrue();
        assertThat(AppConstants.EXCHANGE_DATE_OLDEST.plusDays(1).isEqual(DateRangeValidator.returnValidRange((AppConstants.EXCHANGE_DATE_OLDEST).plusDays(1), latestDate))).isTrue();

    }

    public boolean compareTwoPairsOfDates(LocalDate[] pair1, LocalDate[] pair2) {
        return pair1[0].isEqual(pair2[0]) || pair1[1].isEqual(pair2[1]);
    }

    public LocalDate returnExchangeDateThatExistsInDb(LocalDate date) {

        while (DateRangeValidator.isDateInValidRange(date, latestDate)) {
            if (!exchangeRepository.existsByExchangeDate(date)) {
                date = date.minusDays(1);
            } else {
                return date;
            }
        }
        return AppConstants.EXCHANGE_DATE_OLDEST;
    }
}
