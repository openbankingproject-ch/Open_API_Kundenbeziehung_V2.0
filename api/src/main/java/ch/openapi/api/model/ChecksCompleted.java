package ch.openapi.api.model;

public record ChecksCompleted(
        String sanctionCheck,
        String pepCheck,
        String crimeCheck,
        String creditCheck,
        String adverseMediaCheck
) {}
