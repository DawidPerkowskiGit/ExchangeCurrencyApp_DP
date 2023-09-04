package dpapps.exchangecurrencyapp.exchange.repositories;

import dpapps.exchangecurrencyapp.exchange.model.LocationCurrencyPair;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Location and Currency pairs
 */
@Repository
public interface LocationCurrencyPairRepository extends CrudRepository<LocationCurrencyPair, Integer> {
}
