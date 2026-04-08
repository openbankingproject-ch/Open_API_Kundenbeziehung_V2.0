package ch.openapi.api.model;

public record AddressEntry(
        String addressType,
        String street,
        String houseNumber,
        String postalCode,
        String city,
        String country,
        String canton,
        String validFrom,
        String validTo
) {}
