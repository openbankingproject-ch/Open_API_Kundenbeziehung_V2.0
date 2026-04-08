package ch.openapi.api.model;

public record Education(
        String highestDegree,
        String fieldOfStudy,
        String institution
) {}
