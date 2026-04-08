package ch.openapi.api.model;

public record FinancialProfile(
        TotalAssets totalAssets,
        Income income,
        Employment employment,
        Education education,
        FinancialKnowledge financialKnowledge
) {}
