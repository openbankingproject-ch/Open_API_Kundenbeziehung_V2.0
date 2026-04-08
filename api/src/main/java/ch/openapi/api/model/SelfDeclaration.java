package ch.openapi.api.model;

import java.util.List;

public record SelfDeclaration(
        Boolean economicBeneficiary,
        String taxDomicile,
        Boolean usTaxLiability,
        FatcaDeclaration fatcaDeclaration,
        String tin,
        String sourceOfFunds,
        List<String> nationalities,
        TaxComplianceDeclaration taxComplianceDeclaration
) {}
