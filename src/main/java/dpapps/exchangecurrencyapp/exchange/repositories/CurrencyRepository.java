package dpapps.exchangecurrencyapp.exchange.repositories;

import dpapps.exchangecurrencyapp.exchange.entities.Currency;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Currencies
 */
@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

    @NonNull
    Currency save(@NonNull Currency currency);

    boolean existsById(@NonNull Integer id);

    Currency findCurrencyByIsoName(String name);

    @Query("SELECT c FROM Currency c order by c.isoName")
    List<Currency> getAll();

    @Query("SELECT c.isoName, c.fullName, l.name FROM Currency c, Location l, LocationCurrencyPair lcp WHERE lcp.currency = c AND lcp.location = l")
    List<String[]> getCurrenciesAndLocations();

    @Query("SELECT c FROM Currency c, Exchange e WHERE e.exchangeDate = :date AND c.id = e.currency.id ORDER BY c.isoName")
    List<Currency> getActivelyUsedCurrenciesAtDate(LocalDate date);

}
