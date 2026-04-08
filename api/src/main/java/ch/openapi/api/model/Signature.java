package ch.openapi.api.model;

import java.util.List;

public record Signature(
        String signatureType,
        String signatureStatus,
        List<SignedContract> signedContracts,
        SignatureData signatureData,
        Boolean legallyBinding,
        Boolean mfaCompleted
) {}
