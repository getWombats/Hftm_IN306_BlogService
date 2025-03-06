package ch.hftm.blog.control;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import ch.hftm.blog.model.domain.ValidationResponse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ValidationResponseListener {

    private final Map<String, CompletableFuture<ValidationResponse>> pendingRequests = new ConcurrentHashMap<>();

    @Incoming("validation-response")
    public void processValidationResponse(ValidationResponse response) {
        CompletableFuture<ValidationResponse> future = pendingRequests.remove(response.id());
        if (future != null) {
            future.complete(response);
        }
    }

    public ValidationResponse waitForValidation(String requestId) {
        CompletableFuture<ValidationResponse> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);

        try {
            return future.get(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            pendingRequests.remove(requestId);
            return new ValidationResponse(requestId, false);
        }
    }
}

