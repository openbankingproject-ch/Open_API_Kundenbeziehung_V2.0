package ch.openapi.api.model;

public record TotalAssets(
        Number amount,
        String currency,
        String assetRange,
        String lastUpdated
) {}
