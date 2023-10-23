package dpapps.exchangecurrencyapp.exchange.repositories;

import dpapps.exchangecurrencyapp.exchange.model.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for API keys
 */
@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Integer> {
    ApiKey findByValue(String value);

    boolean existsByValue(String value);
}
