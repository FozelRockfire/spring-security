package t1.study.springsecurity.exception;

import io.jsonwebtoken.security.SignatureException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(value = {
            NotFoundException.class
    })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @Hidden
    public ResponseEntity<ErrorMessage> resourceNotFoundException(RuntimeException exception) {
        log.error("resourceNotFoundException: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorMessage.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .description(exception.getMessage())
                        .currentTime(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            AlreadyExistException.class,
            InvalidMediaTypeException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            ValidationException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @Hidden
    public ResponseEntity<ErrorMessage> validationException(RuntimeException exception) {
        log.error("validationException: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorMessage.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .description(exception.getMessage())
                        .currentTime(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(value = {
            TokenValidationException.class,
            SignatureException.class,
            LogInException.class
    })
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @Hidden
    public ResponseEntity<ErrorMessage> accessDeniedException(RuntimeException exception) {
        log.error("validationException: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorMessage.builder()
                        .statusCode(HttpStatus.FORBIDDEN.value())
                        .description(exception.getMessage())
                        .currentTime(LocalDateTime.now())
                        .build());
    }
}
