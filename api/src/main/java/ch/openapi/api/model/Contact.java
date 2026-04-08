package ch.openapi.api.model;

public record Contact(
        String phoneNumber,
        String mobileNumber,
        String emailAddress,
        String preferredChannel,
        VerificationStatus verificationStatus
) {}
