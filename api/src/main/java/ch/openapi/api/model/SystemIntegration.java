package ch.openapi.api.model;

public record SystemIntegration(
        String coreBankingId,
        String accountNumber,
        String cardIssuanceStatus,
        String documentArchiveReference
) {}
