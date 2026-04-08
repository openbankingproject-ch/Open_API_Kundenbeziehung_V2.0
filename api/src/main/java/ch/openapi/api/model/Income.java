package ch.openapi.api.model;

public record Income(
        Number annualGrossIncome,
        String currency,
        String incomeRange,
        String incomeType,
        String lastUpdated
) {}
