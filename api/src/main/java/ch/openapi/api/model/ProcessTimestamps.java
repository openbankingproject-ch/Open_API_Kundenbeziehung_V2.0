package ch.openapi.api.model;

public record ProcessTimestamps(
        String initialized,
        String productSelected,
        String selfDeclared,
        String basicDataCompleted,
        String financialProfileCompleted,
        String identified,
        String backgroundChecksCompleted,
        String contractsAccepted,
        String signed,
        String finalized
) {}
