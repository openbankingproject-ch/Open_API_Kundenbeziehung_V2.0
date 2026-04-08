package ch.openapi.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/onboarding/*/consent", "/onboarding/*/consent/**").authenticated()
                        .requestMatchers("/oauth2/consent/**").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/oauth2/consent/success", true)
                )
                .csrf(csrf -> csrf.disable())
                .build();
    }
}
