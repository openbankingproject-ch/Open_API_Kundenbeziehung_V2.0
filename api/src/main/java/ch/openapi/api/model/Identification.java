package ch.openapi.api.model;

public record Identification(
        String identificationMethod,
        String referenceNumber,
        String verificationDate,
        DocumentData documentData,
        String verificationLevel,
        BiometricVerification biometricVerification,
        IdentificationAuditTrail auditTrail
) {}
