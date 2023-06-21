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

//    @Query("SELECT c FROM Category c WHERE c.categoryName = ?1")
//    Category findByCategoryName(String name);
    @Query("SELECT MAX(exchangeDate) FROM Exchange")
    LocalDate returnValidLatestExchangeData();

    List<Exchange> findAllByExchangeDate(LocalDate date);


}
