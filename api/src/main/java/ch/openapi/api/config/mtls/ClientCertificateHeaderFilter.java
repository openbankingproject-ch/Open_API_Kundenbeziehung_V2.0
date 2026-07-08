package ch.openapi.api.config.mtls;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class ClientCertificateHeaderFilter extends OncePerRequestFilter {

    public static final String REQUEST_ATTRIBUTE_CERT = "ch.openapi.clientCert";

    private static final Logger log = LoggerFactory.getLogger(ClientCertificateHeaderFilter.class);

    private final MtlsProperties properties;
    private final TrustedProxyMatcher proxyMatcher;

    public ClientCertificateHeaderFilter(MtlsProperties properties, TrustedProxyMatcher proxyMatcher) {
        this.properties = properties;
        this.proxyMatcher = proxyMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (!properties.isEnabled()) {
            chain.doFilter(request, response);
            return;
        }

        String remoteIp = request.getRemoteAddr();
        boolean trustedProxy = proxyMatcher.isTrusted(remoteIp);

        String certHeader = request.getHeader(properties.getCertHeader());
        String verifyHeader = request.getHeader(properties.getVerifyHeader());

        if (!trustedProxy) {
            if (certHeader != null || verifyHeader != null) {
                log.warn("Rejecting request from untrusted source {} carrying SSL headers", remoteIp);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Untrusted proxy");
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        if (verifyHeader == null || !properties.getVerifySuccessValue().equalsIgnoreCase(verifyHeader)) {
            if (properties.isRequireClientCert()) {
                log.warn("Client cert verification not successful (verify={})", verifyHeader);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Client certificate not verified");
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        if (certHeader == null || certHeader.isBlank()) {
            if (properties.isRequireClientCert()) {
                log.warn("Missing client certificate header from trusted proxy {}", remoteIp);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Missing client certificate");
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        X509Certificate cert = parseCertificate(certHeader);
        if (cert == null) {
            log.warn("Failed to parse forwarded client certificate");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid client certificate");
            return;
        }

        request.setAttribute(REQUEST_ATTRIBUTE_CERT, cert);
        chain.doFilter(request, response);
    }

    private X509Certificate parseCertificate(String headerValue) {
        try {
            String decoded = URLDecoder.decode(headerValue, StandardCharsets.UTF_8);
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            try (ByteArrayInputStream stream = new ByteArrayInputStream(decoded.getBytes(StandardCharsets.UTF_8))) {
                return (X509Certificate) factory.generateCertificate(stream);
            }
        } catch (CertificateException | IOException e) {
            log.debug("Cert parse error", e);
            return null;
        }
    }
}
