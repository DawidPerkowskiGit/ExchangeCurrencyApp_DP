package dpapps.exchangecurrencyapp.exchange.repositories;

import dpapps.exchangecurrencyapp.exchange.entities.ApiUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiUserRepository extends CrudRepository<ApiUser, Integer> {

    Optional<ApiUser> findByUserName(String username);

    boolean existsByUserName(String username);
}
