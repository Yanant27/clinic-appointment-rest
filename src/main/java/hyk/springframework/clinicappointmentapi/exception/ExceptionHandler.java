package hyk.springframework.clinicappointmentapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Htoo Yanant Khin
 **/
@Slf4j
@RestControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

//    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
//    public final ResponseEntity<Object> handleAllExceptions(Exception exception, WebRequest request) {
//        ErrorResponse error = new ErrorResponse(exception.getLocalizedMessage());
//        System.out.println(request.getContextPath());
//        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @org.springframework.web.bind.annotation.ExceptionHandler({AccessDeniedException.class})
    public final ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
        ErrorResponse error = new ErrorResponse(exception.getLocalizedMessage());
        System.out.println(request.getContextPath());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({BadCredentialsException.class})
    public final ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException exception, WebRequest request) {
        ErrorResponse error = new ErrorResponse(exception.getLocalizedMessage());
        System.out.println(request.getContextPath());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(ResourceNotFoundException exception, WebRequest request) {
        ErrorResponse error = new ErrorResponse(exception.getLocalizedMessage());
        log.debug(((ServletWebRequest) request).getRequest().toString());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ResourceAlreadyExistException.class)
    public final ResponseEntity<Object> handleResourceAlreadyExistException(ResourceAlreadyExistException exception, WebRequest request) {
        ErrorResponse error = new ErrorResponse(exception.getLocalizedMessage());
        log.debug(((ServletWebRequest) request).getRequest().toString());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            validationErrors.put(fieldName, message);
        });
        FieldValidationErrorResponse error = new FieldValidationErrorResponse("Validation Failed", validationErrors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
