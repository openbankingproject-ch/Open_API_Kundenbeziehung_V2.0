package ch.openapi.api.model;

public record BackgroundChecks(
        ChecksCompleted checksCompleted,
        PepDetails pepDetails,
        String amlRiskClass,
        RiskAssessment riskAssessment,
        String complianceStatus
) {}
