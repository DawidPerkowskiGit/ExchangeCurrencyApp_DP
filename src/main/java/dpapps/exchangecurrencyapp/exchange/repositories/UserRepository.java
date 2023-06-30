package dpapps.exchangecurrencyapp.exchange.repositories;


import dpapps.exchangecurrencyapp.exchange.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);

    @Query("SELECT u.currentRequestsCount FROM User u WHERE u.id = :id")
    public int howManyUsages(@Param("id") Long id);

    @Query("UPDATE User u SET u.currentRequestsCount = :usages WHERE u.id = :id")
    public int increaseNumberOfUsages(@Param("id") Long id,
                                      @Param("usages") int usages);

}