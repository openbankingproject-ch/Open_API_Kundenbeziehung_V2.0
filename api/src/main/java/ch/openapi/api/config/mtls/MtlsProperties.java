package ch.openapi.api.config.mtls;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "openapi.mtls")
public class MtlsProperties {

    private boolean enabled = true;
    private String certHeader = "X-SSL-CERT";
    private String verifyHeader = "X-SSL-VERIFY";
    private String verifySuccessValue = "SUCCESS";
    private List<String> trustedProxies = List.of("127.0.0.1/32", "::1/128");
    private boolean requireClientCert = true;

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public String getCertHeader() { return certHeader; }
    public void setCertHeader(String certHeader) { this.certHeader = certHeader; }

    public String getVerifyHeader() { return verifyHeader; }
    public void setVerifyHeader(String verifyHeader) { this.verifyHeader = verifyHeader; }

    public String getVerifySuccessValue() { return verifySuccessValue; }
    public void setVerifySuccessValue(String verifySuccessValue) { this.verifySuccessValue = verifySuccessValue; }

    public List<String> getTrustedProxies() { return trustedProxies; }
    public void setTrustedProxies(List<String> trustedProxies) { this.trustedProxies = trustedProxies; }

    public boolean isRequireClientCert() { return requireClientCert; }
    public void setRequireClientCert(boolean requireClientCert) { this.requireClientCert = requireClientCert; }
}
