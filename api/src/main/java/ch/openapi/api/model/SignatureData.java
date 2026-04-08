package ch.openapi.api.model;

public record SignatureData(
        String certificate,
        String timestamp,
        String deviceInfo
) {}
