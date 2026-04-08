package ch.openapi.api.model;

public record NfcData(
        Boolean chipVerified,
        String biometricDataHash,
        Number securityFeaturesVerified,
        String chipAuthenticationStatus
) {}
