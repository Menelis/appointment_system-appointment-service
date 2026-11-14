package co.appointment.exception;

import co.appointment.shared.dto.ServiceResponseDto;
import co.appointment.shared.exception.ServiceException;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(final Exception exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.internalServerError()
                .body(ServiceResponseDto.builder()
                        .success(false)
                        .responseMessage(exception.getClass().getSimpleName())
                        .build());
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> handleServiceException(final ServiceException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(exception.getStatus()).body(exception.getResponse());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(final MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();

            // Auto-detect location
            String location = fieldName.contains("requestBody") ? "body" : "header";

            errors.putIfAbsent("field", fieldName);
            errors.putIfAbsent("location", location);
            errors.putIfAbsent("reason", violation.getMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
