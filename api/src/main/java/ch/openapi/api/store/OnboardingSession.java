package ch.openapi.api.store;

import ch.openapi.api.model.*;

import java.util.ArrayList;
import java.util.List;

public class OnboardingSession {

    private final String processId;
    private final String createdAt;
    private final InitializationRequest initializationRequest;
    private final List<AuditEntry> auditTrail = new ArrayList<>();

    private ProductSelection productSelection;
    private Consent consent;
    private SelfDeclaration selfDeclaration;
    private BaseData baseData;
    private FinancialProfile financialProfile;
    private Identification identification;
    private BackgroundChecks backgroundChecks;
    private ContractAcceptance contractAcceptance;
    private Signature signature;

    public OnboardingSession(String processId, String createdAt, InitializationRequest initializationRequest) {
        this.processId = processId;
        this.createdAt = createdAt;
        this.initializationRequest = initializationRequest;
    }

    public String getProcessId() { return processId; }
    public String getCreatedAt() { return createdAt; }
    public InitializationRequest getInitializationRequest() { return initializationRequest; }
    public List<AuditEntry> getAuditTrail() { return auditTrail; }

    public ProductSelection getProductSelection() { return productSelection; }
    public void setProductSelection(ProductSelection productSelection) { this.productSelection = productSelection; }

    public Consent getConsent() { return consent; }
    public void setConsent(Consent consent) { this.consent = consent; }

    public SelfDeclaration getSelfDeclaration() { return selfDeclaration; }
    public void setSelfDeclaration(SelfDeclaration selfDeclaration) { this.selfDeclaration = selfDeclaration; }

    public BaseData getBaseData() { return baseData; }
    public void setBaseData(BaseData baseData) { this.baseData = baseData; }

    public FinancialProfile getFinancialProfile() { return financialProfile; }
    public void setFinancialProfile(FinancialProfile financialProfile) { this.financialProfile = financialProfile; }

    public Identification getIdentification() { return identification; }
    public void setIdentification(Identification identification) { this.identification = identification; }

    public BackgroundChecks getBackgroundChecks() { return backgroundChecks; }
    public void setBackgroundChecks(BackgroundChecks backgroundChecks) { this.backgroundChecks = backgroundChecks; }

    public ContractAcceptance getContractAcceptance() { return contractAcceptance; }
    public void setContractAcceptance(ContractAcceptance contractAcceptance) { this.contractAcceptance = contractAcceptance; }

    public Signature getSignature() { return signature; }
    public void setSignature(Signature signature) { this.signature = signature; }

    public void addAuditEntry(AuditEntry entry) { this.auditTrail.add(entry); }
}
