package ch.openapi.api.model;

public record ExternalIdentityId(
        String provider,
        String id
) {}
