package ch.openapi.api.model;

public record ErrorResponse(
        String code,
        String message
) {}
