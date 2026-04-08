package ch.openapi.api.model;

public record InitializationResponse(
        Boolean cookiesAccepted,
        Boolean cookieConsent,
        Boolean dataProcessingConsent,
        String selectedCountry,
        String serviceType,
        String processId,
        String timestamp
) {}
