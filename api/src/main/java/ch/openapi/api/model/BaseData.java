package ch.openapi.api.model;

public record BaseData(
        Identity identity,
        AddressData address,
        Contact contact
) {}
