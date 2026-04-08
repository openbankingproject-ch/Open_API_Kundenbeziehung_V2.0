package ch.openapi.api.model;

import java.util.List;

public record ProcessMetadata(
        String processId,
        ProcessTimestamps processTimestamps,
        List<AuditEntry> auditTrail,
        SystemIntegration systemIntegration,
        String lastUpdate,
        String status
) {}
