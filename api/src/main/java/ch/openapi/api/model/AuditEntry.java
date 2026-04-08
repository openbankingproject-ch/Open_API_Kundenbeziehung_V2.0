package ch.openapi.api.model;

public record AuditEntry(
        String step,
        String action,
        String originator,
        String timestamp,
        String ipAddress,
        String deviceFingerprint,
        String result
) {}
