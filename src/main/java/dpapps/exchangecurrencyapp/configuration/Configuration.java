package dpapps.exchangecurrencyapp.configuration;

import dpapps.exchangecurrencyapp.exchange.repositories.ApiUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@org.springframework.context.annotation.Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Configuration {

    private final ApiUserRepository apiUserRepository;
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> apiUserRepository.findByUserName(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }
}
