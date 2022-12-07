package hyk.springframework.clinicappointmentapi.dto.appointment;

import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class AppointmentUpdateStatusDTO {
    @NotNull
    private Long id;

    @NotNull
    private AppointmentStatus appointmentStatus;
}
