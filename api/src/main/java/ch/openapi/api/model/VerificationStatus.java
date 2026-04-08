package ch.openapi.api.model;

public record VerificationStatus(
        Boolean phoneVerified,
        Boolean emailVerified,
        String lastVerification
) {}
