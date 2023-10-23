package dpapps.exchangecurrencyapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Module responsible for granting or rejecting users access to specified endpoints, password encoding, filters.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurity {


    private final UserDetailsService userDetailsService;

    @Autowired
    public SpringSecurity(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(
                        AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        (authorize) -> authorize
                                .requestMatchers("/register/**").permitAll()
                                .requestMatchers("/index").permitAll()
                                .requestMatchers("/users").hasRole("ADMIN")
                                .requestMatchers("/profile").authenticated()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("").permitAll()
                                .requestMatchers("/generate").authenticated()
                                .requestMatchers("/api/**").permitAll()
                                .requestMatchers("/health").permitAll()
                                .requestMatchers("/log").hasRole("ADMIN")

                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successForwardUrl("/index")
                        .defaultSuccessUrl("/index")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll());
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}