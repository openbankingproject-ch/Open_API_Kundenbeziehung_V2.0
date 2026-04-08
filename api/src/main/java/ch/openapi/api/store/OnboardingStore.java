package ch.openapi.api.store;

import ch.openapi.api.model.InitializationRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnboardingStore {

    private final ConcurrentHashMap<String, OnboardingSession> sessions = new ConcurrentHashMap<>();

    public OnboardingSession createSession(InitializationRequest request) {
        String processId = UUID.randomUUID().toString();
        String createdAt = Instant.now().toString();
        OnboardingSession session = new OnboardingSession(processId, createdAt, request);
        sessions.put(processId, session);
        return session;
    }

    public Optional<OnboardingSession> getSession(String processId) {
        return Optional.ofNullable(sessions.get(processId));
    }
}
