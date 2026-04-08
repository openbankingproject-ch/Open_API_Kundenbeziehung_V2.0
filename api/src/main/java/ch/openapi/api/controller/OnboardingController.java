package ch.openapi.api.controller;

import ch.openapi.api.exception.ProcessNotFoundException;
import ch.openapi.api.model.*;
import ch.openapi.api.store.OnboardingSession;
import ch.openapi.api.store.OnboardingStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/onboarding")
public class OnboardingController {

    private final OnboardingStore store;

    public OnboardingController(OnboardingStore store) {
        this.store = store;
    }

    @PostMapping("/initialization")
    public ResponseEntity<InitializationResponse> createInitialization(@RequestBody InitializationRequest request) {
        OnboardingSession session = store.createSession(request);
        session.addAuditEntry(new AuditEntry(
                "initialization", "session_created", "system",
                session.getCreatedAt(), null, null, "success"));

        InitializationResponse response = new InitializationResponse(
                request.cookiesAccepted(),
                request.cookieConsent(),
                request.dataProcessingConsent(),
                request.selectedCountry(),
                request.serviceType(),
                session.getProcessId(),
                session.getCreatedAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{processId}/product-selection")
    public ResponseEntity<ProductSelection> updateProductSelection(
            @PathVariable String processId,
            @RequestBody ProductSelection productSelection) {
        OnboardingSession session = findSession(processId);
        session.setProductSelection(productSelection);
        session.addAuditEntry(auditEntry("product-selection", "products_selected"));
        return ResponseEntity.ok(productSelection);
    }

    @GetMapping("/{processId}/product-selection")
    public ResponseEntity<ProductSelection> getProductSelection(@PathVariable String processId) {
        OnboardingSession session = findSession(processId);
        return ResponseEntity.ok(session.getProductSelection());
    }

    @PutMapping("/{processId}/consent")
    public ResponseEntity<Consent> updateConsent(
            @PathVariable String processId,
            @RequestBody Consent consent) {
        OnboardingSession session = findSession(processId);
        session.setConsent(consent);
        session.addAuditEntry(auditEntry("consent", "consent_granted"));
        return ResponseEntity.ok(consent);
    }

    @GetMapping("/{processId}/consent")
    public ResponseEntity<Consent> getConsent(@PathVariable String processId) {
        OnboardingSession session = findSession(processId);
        return ResponseEntity.ok(session.getConsent());
    }

    @PutMapping("/{processId}/self-declaration")
    public ResponseEntity<SelfDeclaration> updateSelfDeclaration(
            @PathVariable String processId,
            @RequestBody SelfDeclaration selfDeclaration) {
        OnboardingSession session = findSession(processId);
        session.setSelfDeclaration(selfDeclaration);
        session.addAuditEntry(auditEntry("self-declaration", "self_declaration_submitted"));
        return ResponseEntity.ok(selfDeclaration);
    }

    @GetMapping("/{processId}/self-declaration")
    public ResponseEntity<SelfDeclaration> getSelfDeclaration(@PathVariable String processId) {
        OnboardingSession session = findSession(processId);
        return ResponseEntity.ok(session.getSelfDeclaration());
    }

    @PutMapping("/{processId}/base-data")
    public ResponseEntity<BaseData> updateBaseData(
            @PathVariable String processId,
            @RequestBody BaseData baseData) {
        OnboardingSession session = findSession(processId);
        session.setBaseData(baseData);
        session.addAuditEntry(auditEntry("base-data", "base_data_updated"));
        return ResponseEntity.ok(baseData);
    }

    @GetMapping("/{processId}/base-data")
    public ResponseEntity<BaseData> getBaseData(@PathVariable String processId) {
        OnboardingSession session = findSession(processId);
        return ResponseEntity.ok(session.getBaseData());
    }

    @PutMapping("/{processId}/financial-profile")
    public ResponseEntity<FinancialProfile> updateFinancialProfile(
            @PathVariable String processId,
            @RequestBody FinancialProfile financialProfile) {
        OnboardingSession session = findSession(processId);
        session.setFinancialProfile(financialProfile);
        session.addAuditEntry(auditEntry("financial-profile", "financial_profile_updated"));
        return ResponseEntity.ok(financialProfile);
    }

    @GetMapping("/{processId}/financial-profile")
    public ResponseEntity<FinancialProfile> getFinancialProfile(@PathVariable String processId) {
        OnboardingSession session = findSession(processId);
        return ResponseEntity.ok(session.getFinancialProfile());
    }

    @PutMapping("/{processId}/identification")
    public ResponseEntity<Identification> updateIdentification(
            @PathVariable String processId,
            @RequestBody Identification identification) {
        OnboardingSession session = findSession(processId);
        session.setIdentification(identification);
        session.addAuditEntry(auditEntry("identification", "identification_completed"));
        return ResponseEntity.ok(identification);
    }

    @GetMapping("/{processId}/identification")
    public ResponseEntity<Identification> getIdentification(@PathVariable String processId) {
        OnboardingSession session = findSession(processId);
        return ResponseEntity.ok(session.getIdentification());
    }

    @GetMapping("/{processId}/background-checks")
    public ResponseEntity<BackgroundChecks> getBackgroundChecks(@PathVariable String processId) {
        OnboardingSession session = findSession(processId);
        String now = Instant.now().toString();

        BackgroundChecks checks = new BackgroundChecks(
                new ChecksCompleted("passed", "passed", "passed", "passed", "passed"),
                new PepDetails("no", null, now),
                "low",
                new RiskAssessment("low", 5, List.of(), now),
                "approved");

        session.setBackgroundChecks(checks);
        session.addAuditEntry(auditEntry("background-checks", "background_checks_completed"));
        return ResponseEntity.ok(checks);
    }

    @PutMapping("/{processId}/contract-acceptance")
    public ResponseEntity<ContractAcceptance> updateContractAcceptance(
            @PathVariable String processId,
            @RequestBody ContractAcceptance contractAcceptance) {
        OnboardingSession session = findSession(processId);
        session.setContractAcceptance(contractAcceptance);
        session.addAuditEntry(auditEntry("contract-acceptance", "contracts_accepted"));
        return ResponseEntity.ok(contractAcceptance);
    }

    @GetMapping("/{processId}/contract-acceptance")
    public ResponseEntity<ContractAcceptance> getContractAcceptance(@PathVariable String processId) {
        OnboardingSession session = findSession(processId);
        return ResponseEntity.ok(session.getContractAcceptance());
    }

    @PutMapping("/{processId}/signature")
    public ResponseEntity<Signature> updateSignature(
            @PathVariable String processId,
            @RequestBody Signature signature) {
        OnboardingSession session = findSession(processId);
        session.setSignature(signature);
        session.addAuditEntry(auditEntry("signature", "signature_completed"));
        return ResponseEntity.ok(signature);
    }

    @GetMapping("/{processId}/signature")
    public ResponseEntity<Signature> getSignature(@PathVariable String processId) {
        OnboardingSession session = findSession(processId);
        return ResponseEntity.ok(session.getSignature());
    }

    @GetMapping("/{processId}/metadata")
    public ResponseEntity<ProcessMetadata> getMetadata(@PathVariable String processId) {
        OnboardingSession session = findSession(processId);
        String now = Instant.now().toString();

        String status = session.getSignature() != null ? "completed" : "in_progress";

        ProcessTimestamps timestamps = new ProcessTimestamps(
                session.getCreatedAt(),
                session.getProductSelection() != null ? now : null,
                session.getSelfDeclaration() != null ? now : null,
                session.getBaseData() != null ? now : null,
                session.getFinancialProfile() != null ? now : null,
                session.getIdentification() != null ? now : null,
                session.getBackgroundChecks() != null ? now : null,
                session.getContractAcceptance() != null ? now : null,
                session.getSignature() != null ? now : null,
                null);

        ProcessMetadata metadata = new ProcessMetadata(
                session.getProcessId(),
                timestamps,
                session.getAuditTrail(),
                null,
                now,
                status);

        return ResponseEntity.ok(metadata);
    }

    private OnboardingSession findSession(String processId) {
        return store.getSession(processId)
                .orElseThrow(() -> new ProcessNotFoundException(processId));
    }

    private AuditEntry auditEntry(String step, String action) {
        return new AuditEntry(step, action, "user", Instant.now().toString(), null, null, "success");
    }
}
