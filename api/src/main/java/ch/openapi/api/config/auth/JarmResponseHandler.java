package ch.openapi.api.config.auth;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class JarmResponseHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(JarmResponseHandler.class);

    private final JWKSource<SecurityContext> jwkSource;
    private final AuthorizationServerSettings settings;

    public JarmResponseHandler(JWKSource<SecurityContext> jwkSource, AuthorizationServerSettings settings) {
        this.jwkSource = jwkSource;
        this.settings = settings;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                         Authentication authentication) throws IOException {
        if (!(authentication instanceof OAuth2AuthorizationCodeRequestAuthenticationToken token)) {
            log.debug("Unexpected authentication type for JARM: {}", authentication.getClass());
            return;
        }

        String redirectUri = token.getRedirectUri();
        String code = token.getAuthorizationCode() != null ? token.getAuthorizationCode().getTokenValue() : null;
        String state = token.getState();

        String jarm;
        try {
            jarm = buildJarm(token.getClientId(), code, state);
        } catch (JOSEException e) {
            throw new IOException("Failed to sign JARM response", e);
        }

        String target = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("response", jarm)
                .build(true)
                .toUriString();
        response.sendRedirect(target);
    }

    private String buildJarm(String clientId, String code, String state) throws JOSEException {
        RSAKey signingKey = selectSigningKey();
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer(settings.getIssuer())
                .audience(clientId)
                .expirationTime(Date.from(Instant.now().plusSeconds(60)))
                .issueTime(Date.from(Instant.now()))
                .claim("code", code)
                .claim("state", state)
                .build();

        SignedJWT jwt = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.PS256).keyID(signingKey.getKeyID()).build(),
                claims);
        jwt.sign(new RSASSASigner(signingKey.toPrivateKey()));
        return jwt.serialize();
    }

    private RSAKey selectSigningKey() throws JOSEException {
        JWKMatcher matcher = new JWKMatcher.Builder().keyUse(KeyUse.SIGNATURE).build();
        List<JWK> keys = jwkSource.get(new JWKSelector(matcher), null);
        if (keys.isEmpty()) {
            keys = jwkSource.get(new JWKSelector(new JWKMatcher.Builder().build()), null);
        }
        if (keys.isEmpty()) {
            throw new JOSEException("No signing key available for JARM");
        }
        return (RSAKey) keys.get(0);
    }
}
