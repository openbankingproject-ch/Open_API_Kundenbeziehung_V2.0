package ch.openapi.api.config.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.UUID;

@Configuration
public class RegisteredClientLoader {

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient demoTpp = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("demo-tpp")
                .clientIdIssuedAt(java.time.Instant.now())
                .clientName("Demo Third-Party Provider")
                .clientAuthenticationMethod(ClientAuthenticationMethod.PRIVATE_KEY_JWT)
                .clientAuthenticationMethod(ClientAuthenticationMethod.TLS_CLIENT_AUTH)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("https://demo-tpp.local/callback")
                .scope("openid")
                .scope("accounts:read")
                .scope("payments:initiate")
                .scope("customer-relationship")
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .requireProofKey(true)
                        .tokenEndpointAuthenticationSigningAlgorithm(SignatureAlgorithm.PS256)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .authorizationCodeTimeToLive(Duration.ofSeconds(30))
                        .accessTokenTimeToLive(Duration.ofHours(24))
                        .refreshTokenTimeToLive(Duration.ofDays(30))
                        .reuseRefreshTokens(false)
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .idTokenSignatureAlgorithm(SignatureAlgorithm.PS256)
                        .x509CertificateBoundAccessTokens(true)
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(demoTpp);
    }
}
