package dpapps.exchangecurrencyapp.exchange.repositories;

import dpapps.exchangecurrencyapp.exchange.model.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for currency locations
 */
@Repository
public interface LocationRepository extends CrudRepository<Location, Integer> {
}
