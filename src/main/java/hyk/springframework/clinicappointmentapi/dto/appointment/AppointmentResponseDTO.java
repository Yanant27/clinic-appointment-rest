package hyk.springframework.clinicappointmentapi.dto.appointment;

import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class AppointmentResponseDTO extends AppointmentRegistrationDTO {
    private Long id;

    private String doctorName;

    private String specialization;

    private String patientName;

    private String patientPhoneNumber;

    private AppointmentStatus appointmentStatus;

    private String createdBy;

    private String modifiedBy;
}
