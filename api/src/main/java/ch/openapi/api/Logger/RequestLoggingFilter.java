package ch.openapi.api.Logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

// Logs each HTTP request with a generated request id (available in MDC as "requestId")
// so that downstream log statements can be correlated to a single request.
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private static final String MDC_REQUEST_ID = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestId = request.getHeader(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }
        MDC.put(MDC_REQUEST_ID, requestId);
        response.setHeader(REQUEST_ID_HEADER, requestId);

        long start = System.currentTimeMillis();
        try {
            logger.info("--> {} {}{}", request.getMethod(), request.getRequestURI(),
                    request.getQueryString() == null ? "" : "?" + request.getQueryString());
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            int status = response.getStatus();
            if (status >= 500) {
                logger.error("<-- {} {} {} ({} ms)", request.getMethod(), request.getRequestURI(), status, duration);
            } else if (status >= 400) {
                logger.warn("<-- {} {} {} ({} ms)", request.getMethod(), request.getRequestURI(), status, duration);
            } else {
                logger.info("<-- {} {} {} ({} ms)", request.getMethod(), request.getRequestURI(), status, duration);
            }
            MDC.remove(MDC_REQUEST_ID);
        }
    }
}
