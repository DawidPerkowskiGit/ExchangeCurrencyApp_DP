package dpapps.exchangecurrencyapp.exchange.repositories;

import dpapps.exchangecurrencyapp.exchange.entities.Currency;
import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Exchanges
 */
@Repository
public interface ExchangeRepository extends CrudRepository<Exchange, Integer> {
    boolean existsByExchangeDate(LocalDate date);

    @Query("SELECT e FROM Exchange e WHERE e.exchangeDate = :date ORDER BY e.currency.id")
    List<Exchange> findAllByExchangeDateOrderByCurrencyDesc(@Param("date") LocalDate date);

    @Query("SELECT e FROM Exchange e INNER JOIN Currency c ON e.currency = c WHERE e.exchangeDate = :date AND c.isoName = :currency ORDER BY e.currency.id")
    List<Exchange> findAllByExchangeDateAndCurrencyOrderByExchangeDate(@Param("date") LocalDate date, @Param("currency") String currency);

    @Query("SELECT e FROM Exchange e INNER JOIN Currency c ON e.currency = c WHERE e.exchangeDate = :date AND c.isoName = :currency")
    Exchange findByExchangeDateAndCurrency_IsoName(@Param("date") LocalDate date, @Param("currency") String currency);

    boolean existsByExchangeDateAndCurrency_IsoName(LocalDate date, String iso_name);

    @Query("SELECT MAX(e.exchangeDate) FROM Exchange e")
    LocalDate getLatestExchangeDate();


}
