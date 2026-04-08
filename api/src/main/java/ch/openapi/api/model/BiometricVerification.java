package ch.openapi.api.model;

public record BiometricVerification(
        Number livenessScore,
        Number faceMatchScore,
        Number documentAuthenticityScore,
        Number securityFeaturesChecked,
        Number securityFeaturesVerified
) {}
