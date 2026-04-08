package ch.openapi.api.model;

public record IdentificationAuditTrail(
        String videoReference,
        String documentScanReference,
        String timestamp,
        String originator
) {}
