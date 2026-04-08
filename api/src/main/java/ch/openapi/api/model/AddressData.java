package ch.openapi.api.model;

public record AddressData(
        AddressEntry residentialAddress,
        AddressEntry correspondenceAddress
) {}
