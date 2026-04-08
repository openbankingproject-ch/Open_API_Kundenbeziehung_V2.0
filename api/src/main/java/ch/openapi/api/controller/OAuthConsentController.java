package ch.openapi.api.controller;

import ch.openapi.api.exception.ProcessNotFoundException;
import ch.openapi.api.model.AuditEntry;
import ch.openapi.api.model.Consent;
import ch.openapi.api.store.OnboardingSession;
import ch.openapi.api.store.OnboardingStore;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/oauth2/consent")
public class OAuthConsentController {

    private static final String SESSION_PROCESS_ID = "consentProcessId";

    private final OnboardingStore store;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public OAuthConsentController(OnboardingStore store, OAuth2AuthorizedClientService authorizedClientService) {
        this.store = store;
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/grant/{processId}")
    public String initiateConsent(@PathVariable String processId, HttpSession session) {
        store.getSession(processId)
                .orElseThrow(() -> new ProcessNotFoundException(processId));
        session.setAttribute(SESSION_PROCESS_ID, processId);
        return "redirect:/oauth2/authorization/google";
    }

    @GetMapping("/success")
    public ResponseEntity<Consent> handleOAuthSuccess(OAuth2AuthenticationToken authentication, HttpSession session) {
        String processId = (String) session.getAttribute(SESSION_PROCESS_ID);
        if (processId == null) {
            return ResponseEntity.badRequest().build();
        }

        OnboardingSession onboardingSession = store.getSession(processId)
                .orElseThrow(() -> new ProcessNotFoundException(processId));

        List<String> grantedScopes = extractScopes(authentication);
        String now = Instant.now().toString();

        Consent consent = new Consent(
                UUID.randomUUID().toString(),
                List.of("initialization", "productSelection", "selfDeclaration", "basicData",
                        "financialProfile", "identification", "backgroundChecks", "contracts", "signature"),
                List.of("account_opening", "kyc_verification"),
                grantedScopes,
                "active",
                now,
                Instant.now().plus(365, ChronoUnit.DAYS).toString()
        );

        onboardingSession.setConsent(consent);
        onboardingSession.addAuditEntry(new AuditEntry(
                "consent", "consent_granted_via_oauth", "system",
                now, null, null, "success"));

        session.removeAttribute(SESSION_PROCESS_ID);

        return ResponseEntity.ok(consent);
    }

    private List<String> extractScopes(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());

        if (client != null && client.getAccessToken() != null
                && client.getAccessToken().getScopes() != null) {
            return new ArrayList<>(client.getAccessToken().getScopes());
        }
        return List.of("openid", "profile", "email");
    }
}
