package dpapps.exchangecurrencyapp.exchange.repositories;

import dpapps.exchangecurrencyapp.exchange.entities.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for API keys
 */
@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Integer> {
    public ApiKey findByValue(String value);

    public boolean existsByValue(String value);
}
