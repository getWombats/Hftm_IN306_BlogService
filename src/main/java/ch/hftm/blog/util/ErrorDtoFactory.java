package ch.hftm.blog.util;

import java.util.UUID;

import ch.hftm.blog.model.dto.ErrorDTO;

public class ErrorDtoFactory {
    public static ErrorDTO createErrorDto(Throwable ex, String message) {
        return new ErrorDTO(UUID.randomUUID(), ex.getClass().getSimpleName(), message);
    }
}