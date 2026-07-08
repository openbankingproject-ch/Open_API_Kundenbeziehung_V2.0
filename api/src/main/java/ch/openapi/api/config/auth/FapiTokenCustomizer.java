package ch.openapi.api.config.auth;

import ch.openapi.api.config.mtls.ClientCertificateHeaderFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class FapiTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private static final Logger log = LoggerFactory.getLogger(FapiTokenCustomizer.class);

    @Override
    public void customize(JwtEncodingContext context) {
        if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
            return;
        }

        X509Certificate cert = currentClientCertificate();
        if (cert == null) {
            log.debug("No client certificate present in request; cnf claim not added");
            return;
        }

        String thumbprint = computeSha256Thumbprint(cert);
        if (thumbprint == null) {
            return;
        }

        Map<String, Object> cnf = new LinkedHashMap<>();
        cnf.put("x5t#S256", thumbprint);
        context.getClaims().claim("cnf", cnf);

        Object authorizationDetails = context.getAuthorization() == null
                ? null
                : context.getAuthorization().getAttribute("authorization_details");
        if (authorizationDetails != null) {
            context.getClaims().claim("authorization_details", authorizationDetails);
        }
    }

    private X509Certificate currentClientCertificate() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null;
        }
        HttpServletRequest request = attrs.getRequest();
        Object cert = request.getAttribute(ClientCertificateHeaderFilter.REQUEST_ATTRIBUTE_CERT);
        return cert instanceof X509Certificate x509 ? x509 : null;
    }

    private String computeSha256Thumbprint(X509Certificate cert) {
        try {
            byte[] der = cert.getEncoded();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(der);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            log.warn("Failed to compute cert thumbprint", e);
            return null;
        }
    }
}
