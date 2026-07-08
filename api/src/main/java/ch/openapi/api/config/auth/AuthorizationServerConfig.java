package ch.openapi.api.config.auth;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.http.MediaType;

@Configuration
@EnableConfigurationProperties(AuthorizationServerConfig.AuthServerProperties.class)
public class AuthorizationServerConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http,
            JWKSource<SecurityContext> jwkSource,
            AuthorizationServerSettings settings) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        JarmResponseHandler jarmHandler = new JarmResponseHandler(jwkSource, settings);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .pushedAuthorizationRequestEndpoint(Customizer.withDefaults())
                .authorizationEndpoint(authz -> authz.authorizationResponseHandler(jarmHandler))
                .oidc(Customizer.withDefaults());

        http
                .exceptionHandling(ex -> ex
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)))
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings(AuthServerProperties properties) {
        return AuthorizationServerSettings.builder()
                .issuer(properties.getIssuer())
                .build();
    }

    @ConfigurationProperties(prefix = "openapi.auth-server")
    public static class AuthServerProperties {
        private String issuer = "https://api.openapi.local";

        public String getIssuer() { return issuer; }
        public void setIssuer(String issuer) { this.issuer = issuer; }
    }
}
