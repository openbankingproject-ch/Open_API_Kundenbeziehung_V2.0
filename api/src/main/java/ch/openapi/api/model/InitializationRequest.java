package ch.openapi.api.model;

public record InitializationRequest(
        Boolean cookiesAccepted,
        Boolean cookieConsent,
        Boolean dataProcessingConsent,
        String selectedCountry,
        String serviceType
) {}
