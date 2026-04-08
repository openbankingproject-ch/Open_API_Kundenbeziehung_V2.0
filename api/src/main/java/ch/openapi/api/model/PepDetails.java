package ch.openapi.api.model;

public record PepDetails(
        String pepStatus,
        String pepCategory,
        String lastChecked
) {}
