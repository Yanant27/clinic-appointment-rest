package hyk.springframework.clinicappointmentapi.dto.appointment;

import hyk.springframework.clinicappointmentapi.dto.BaseDTO;
import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
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

    public AppointmentResponseDTO(Long id, Long doctorId, String doctorName, String specialization, Long patientId, String patientName, String patientPhoneNumber, LocalDate appointmentDate, AppointmentStatus appointmentStatus, Long scheduleId, String timeslot) {
        super(id);
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientPhoneNumber = patientPhoneNumber;
        this.appointmentDate = appointmentDate;
        this.appointmentStatus = appointmentStatus;
        this.scheduleId = scheduleId;
        this.timeslot = timeslot;
    }
}
