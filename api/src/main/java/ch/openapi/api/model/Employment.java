package ch.openapi.api.model;

public record Employment(
        String profession,
        String employer,
        String employmentType,
        Number yearsWithEmployer,
        String industry
) {}
