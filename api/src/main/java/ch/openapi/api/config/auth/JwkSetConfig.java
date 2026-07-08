package ch.openapi.api.config.auth;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
public class JwkSetConfig {

    private static final Logger log = LoggerFactory.getLogger(JwkSetConfig.class);

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateRsa();
        log.info("Generated FAPI signing JWK with kid={}", rsaKey.getKeyID());
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return NimbusJwtDecoder.withPublicKey(extractPublicKey(jwkSource)).build();
    }

    private RSAPublicKey extractPublicKey(JWKSource<SecurityContext> jwkSource) {
        try {
            JWKSet set = ((ImmutableJWKSet<SecurityContext>) jwkSource).getJWKSet();
            RSAKey rsaKey = (RSAKey) set.getKeys().get(0);
            return rsaKey.toRSAPublicKey();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot extract RSA public key from JWKSource", e);
        }
    }

    private static RSAKey generateRsa() {
        KeyPair keyPair = generateRsaKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    private static KeyPair generateRsaKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return generator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot generate RSA keypair", e);
        }
    }
}
