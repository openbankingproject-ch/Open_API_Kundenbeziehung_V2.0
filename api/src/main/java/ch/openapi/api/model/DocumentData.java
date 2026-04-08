package ch.openapi.api.model;

public record DocumentData(
        String documentType,
        String documentNumber,
        String issuingAuthority,
        String issueDate,
        String issuePlace,
        String expiryDate,
        String mrz,
        NfcData nfcData
) {}
