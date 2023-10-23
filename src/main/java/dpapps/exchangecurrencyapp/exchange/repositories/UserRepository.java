package dpapps.exchangecurrencyapp.exchange.repositories;


import dpapps.exchangecurrencyapp.exchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for user data
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);

    @Query("SELECT u.currentRequestsCount FROM User u WHERE u.id = :id")
    int howManyUsages(@Param("id") Long id);

    @Modifying
    @Query("UPDATE User u SET u.currentRequestsCount = :usages WHERE u.id = :id")
    int increaseNumberOfUsages(@Param("id") Long id, @Param("usages") int usages);

}