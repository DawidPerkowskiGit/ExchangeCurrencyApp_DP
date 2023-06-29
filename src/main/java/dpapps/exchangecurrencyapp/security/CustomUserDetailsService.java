package dpapps.exchangecurrencyapp.security;

import dpapps.exchangecurrencyapp.exchange.entities.ApiUser;
import dpapps.exchangecurrencyapp.exchange.entities.Role;
import dpapps.exchangecurrencyapp.exchange.repositories.ApiUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private ApiUserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(ApiUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        ApiUser user = userRepository.findByUserName(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not exists by Username"));

        return new CustomUserDetails(user);
    }


    private Collection < ? extends GrantedAuthority> mapRolesToAuthorities(Collection <Role> roles) {
        Collection < ? extends GrantedAuthority> mapRoles = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
        return mapRoles;
    }
}
