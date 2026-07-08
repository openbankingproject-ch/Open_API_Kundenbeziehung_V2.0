package ch.openapi.api.config.mtls;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@EnableConfigurationProperties(MtlsProperties.class)
public class MtlsConfig {

    @Bean
    public TrustedProxyMatcher trustedProxyMatcher(MtlsProperties properties) {
        return new TrustedProxyMatcher(properties.getTrustedProxies());
    }

    @Bean
    public FilterRegistrationBean<ClientCertificateHeaderFilter> clientCertificateHeaderFilter(
            MtlsProperties properties, TrustedProxyMatcher matcher) {
        FilterRegistrationBean<ClientCertificateHeaderFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ClientCertificateHeaderFilter(properties, matcher));
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        return registration;
    }
}
