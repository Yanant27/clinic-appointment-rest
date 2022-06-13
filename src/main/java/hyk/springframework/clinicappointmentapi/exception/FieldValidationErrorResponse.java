package hyk.springframework.clinicappointmentapi.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author Htoo Yanant Khin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FieldValidationErrorResponse extends ErrorResponse {
    private Map<String, String> validation_errors;

    public FieldValidationErrorResponse(String errorMessage, Map<String, String> validation_errors) {
        super(errorMessage);
        this.validation_errors = validation_errors;
    }
}
