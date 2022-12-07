package hyk.springframework.clinicappointmentapi.dto.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class AppointmentRegistrationDTO {
    @NotNull
    private Long doctorId;

    @NotNull
    private Long patientId;

    @NotNull
    private String timeslot;

    @NotNull
    @Future
    @JsonFormat(pattern="yyyy-MM-dd", shape=JsonFormat.Shape.STRING)
    private LocalDate appointmentDate;
}
