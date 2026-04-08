package ch.openapi.api.model;

public record TaxComplianceDeclaration(
        Boolean confirmed,
        String declarationDate
) {}
