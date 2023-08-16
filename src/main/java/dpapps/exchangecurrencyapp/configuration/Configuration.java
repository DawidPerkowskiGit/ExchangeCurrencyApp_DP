package dpapps.exchangecurrencyapp.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;
import org.thymeleaf.spring6.SpringTemplateEngine;

@org.springframework.context.annotation.Configuration
@EnableScheduling
@RequiredArgsConstructor
/**
 * Applications configuration. Scheduling is enabled
 */
public class Configuration {
}