package org.base.config;

import org.base.dto.common.MessageResponseDTO;
import org.base.exception.SystemException;
import org.base.exception.UnauthorizationException;
import org.base.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = {SystemException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageResponseDTO resourceLogicException(SystemException ex, WebRequest request) {
        MessageResponseDTO response = new MessageResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            ex.getMessage(), null, request.getDescription(false));

        return response;
    }

    @ExceptionHandler(value = {ValidationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public MessageResponseDTO resourceValidationException(ValidationException ex, WebRequest request) {
        MessageResponseDTO response = new MessageResponseDTO(HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(), null, request.getDescription(false));

        return response;
    }

    @ExceptionHandler(value = {UnauthorizationException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public MessageResponseDTO resourceUnauthorizationException(UnauthorizationException ex, WebRequest request) {
        MessageResponseDTO response = new MessageResponseDTO(HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(), null, request.getDescription(false));

        return response;
    }
}