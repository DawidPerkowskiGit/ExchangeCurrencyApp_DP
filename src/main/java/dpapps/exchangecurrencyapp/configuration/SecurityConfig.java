
package dpapps.exchangecurrencyapp.configuration;

import dpapps.exchangecurrencyapp.exchange.repositories.ApiUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig{

    @Autowired
    private DataSource dataSource;
    private final ApiUserRepository apiUserRepository;

    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public SecurityConfig(ApiUserRepository apiUserRepository) {
        this.apiUserRepository = apiUserRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/register**").permitAll()
//                                .requestMatchers("/currencies**").permitAll()
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .usernameParameter("user_name")
                                .passwordParameter("password")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().passwordEncoder(passwordEncoder())
                .dataSource(dataSource)
                .usersByUsernameQuery("select user_name, password from api_user where user_name = ?");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
/*
    @Bean
    public SecurityFilterChain currencyFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/currencies**")
                        .authenticated());
        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain registerFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register**")
                        .permitAll());
        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain loginFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .formLogin((formLogin) ->
                        formLogin
                                .usernameParameter("user_name")
                                .passwordParameter("password")
                                .loginPage("/login")
                );
        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain logoutFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .logout((logout) -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true));
        return httpSecurity.build();
    }*/
/*    @Bean
    public SecurityFilterChain loginFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                        .requestMatchers("/currencies**")
                                .permitAll().anyRequest().authenticated();
        httpSecurity
                .authorizeRequests()
                        .requestMatchers("/register**")
                                .permitAll().anyRequest().authenticated();
        httpSecurity
                .formLogin((formLogin) ->
                        formLogin
                                .usernameParameter("user_name")
                                .passwordParameter("password")
                                .loginPage("/login")
                );
        httpSecurity
                .logout((logout) -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll());
        return httpSecurity.build();
    }*/

/*    @Bean
    public SecurityFilterChain loginFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .requestMatchers("/currencies**")
                .permitAll().anyRequest().authenticated()
                .and()
                .authorizeRequests()
                .requestMatchers("/register**")
                .permitAll().anyRequest().authenticated()
                .and()
                .formLogin((formLogin) ->
                        formLogin
                                .usernameParameter("user_name")
                                .passwordParameter("password")
                                .loginPage("/login"))
                .logout((logout) -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll());
        return httpSecurity.build();
    }*/

/*    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(this.apiUserRepository);
    }*/



/*
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
*/


/*    @Bean
    public UserDetailsService userDetailsService() {
        return new MyDatabaseUserDetailsService(); // (1)
    }*/


}


