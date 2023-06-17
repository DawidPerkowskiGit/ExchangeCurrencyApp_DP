package dpapps.exchangecurrencyapp.exchange.repositories;

import dpapps.exchangecurrencyapp.exchange.entities.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

    Currency save(Currency currency);

    boolean existsByIsoName(String name);
    boolean existsById(Integer id);

    Currency findCurrencyByIsoName(String name);



}
