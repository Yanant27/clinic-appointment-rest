package hyk.springframework.clinicappointmentapi.dto.appointment;

import hyk.springframework.clinicappointmentapi.dto.BaseDTO;
import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class AppointmentResponseDTO extends BaseDTO {
    /*
    Autofill doctor data, input field in disabled.
     */
    private Long doctorId;

    private String doctorName;

    private String specialization;

    private Long patientId;

    private String patientName;

    private String patientPhoneNumber;

    private LocalDate appointmentDate;

    private AppointmentStatus appointmentStatus;

    private Long scheduleId;

    private String timeslot;
}
