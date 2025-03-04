package ch.hftm.blog.control;

import java.util.UUID;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import ch.hftm.blog.model.domain.ValidationRequest;
import ch.hftm.blog.model.domain.ValidationResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ValidationService {
    @Inject
    @Channel("validation-request")
    Emitter<ValidationRequest> validationRequestEmitter;

    @Inject
    ValidationResponseListener validationResponseListener;

    public ValidationResponse validateBlogContent(String content) {
        String validationRequestId = UUID.randomUUID().toString();
        validationRequestEmitter.send(new ValidationRequest(validationRequestId, content));
        return validationResponseListener.waitForValidation(validationRequestId);
    }
}
