package dpapps.exchangecurrencyapp.exchange.repositories;

import dpapps.exchangecurrencyapp.exchange.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
