package ch.openapi.api.model;

public record ContractItem(
        String contractType,
        String contractId,
        String version,
        Boolean accepted,
        String acceptanceTimestamp,
        String productId
) {}
