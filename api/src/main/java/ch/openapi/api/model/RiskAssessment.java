package ch.openapi.api.model;

import java.util.List;

public record RiskAssessment(
        String overallRisk,
        Number riskScore,
        List<String> riskFactors,
        String lastAssessment
) {}
