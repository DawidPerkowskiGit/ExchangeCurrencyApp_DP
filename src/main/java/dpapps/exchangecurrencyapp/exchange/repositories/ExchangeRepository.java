package dpapps.exchangecurrencyapp.exchange.repositories;

import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExchangeRepository extends CrudRepository<Exchange, Integer> {
    boolean existsByExchangeDate(LocalDate date);

    @Query("SELECT MAX(exchangeDate) FROM Exchange")
    LocalDate returnValidLatestExchangeData();

    @Query("SELECT e FROM Exchange e WHERE e.exchangeDate = ?1 ORDER BY e.currency.id")
    List<Exchange> findAllByExchangeDateOrderByCurrencyDesc(LocalDate date);

    @Query("SELECT e FROM Exchange e INNER JOIN Currency c ON e.currency = c WHERE e.exchangeDate = ?1 AND c.isoName = ?2 ORDER BY e.currency.id")
    List<Exchange> findAllByExchangeDateAndCurrencyOrderByExchangeDate(LocalDate date, String currency);


}
