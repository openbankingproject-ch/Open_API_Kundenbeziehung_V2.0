package ch.openapi.api.model;

public record SignedContract(
        String contractType,
        String version,
        String signatureTimestamp,
        String signatureId
) {}
