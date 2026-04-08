package ch.openapi.api.model;

import java.util.List;

public record ContractAcceptance(
        List<ContractItem> contracts,
        Boolean termsAccepted,
        String contractType,
        String contractVersion
) {}
