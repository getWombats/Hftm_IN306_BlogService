package ch.hftm.blog.control;

import ch.hftm.blog.model.domain.ServiceResponse;
import ch.hftm.blog.model.dto.ErrorDTO;
import ch.hftm.blog.util.ErrorDtoFactory;
import io.quarkus.logging.Log;

public abstract class ServiceBase {
    protected <T> void handleError(ServiceResponse<T> serviceResponse, Exception ex, String message) {
        ErrorDTO err = ErrorDtoFactory.createErrorDto(ex, message);
        Log.error(err.getMessage() + " / Trace-ID: " + err.getTraceId(), ex);
        serviceResponse.setError(ex);
        serviceResponse.setErrorDto(err);
        serviceResponse.setError(ex);
    }
}
