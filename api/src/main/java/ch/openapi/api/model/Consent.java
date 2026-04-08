package ch.openapi.api.model;

import java.util.List;

public record Consent(
        String consentId,
        List<String> dataCategories,
        List<String> purposes,
        List<String> grantedScopes,
        String status,
        String grantedAt,
        String expiresAt
) {}
