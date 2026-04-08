package ch.openapi.api.model;

import java.util.List;

public record PersonalData(
        String title,
        String firstName,
        String lastName,
        String gender,
        String dateOfBirth,
        String placeOfBirth,
        String placeOfOrigin,
        List<String> nationality,
        String maritalStatus,
        ExternalIdentityId externalIdentityId
) {}
