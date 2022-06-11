package hyk.springframework.clinicappointmentapi.web.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Htoo Yanant Khin
 */
@Data
@NoArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.timestamp = LocalDateTime.now();
        this.errorMessage = errorMessage;
    }
}
