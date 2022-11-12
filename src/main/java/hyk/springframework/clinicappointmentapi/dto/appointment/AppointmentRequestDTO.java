package hyk.springframework.clinicappointmentapi.dto.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import hyk.springframework.clinicappointmentapi.dto.BaseDTO;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientRequestDTO;
import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class AppointmentRequestDTO extends BaseDTO {
    /*
    Autofill doctor data, input field in disabled.
     */
//    private DoctorSummaryDTO doctorSummaryDTO;
    @NotNull
    private Long doctorId;

    /*
    For new patient, fill all data.
    For old patient, fill patient id only and autofill for patient data in appointment.
    At client side, use radio button to check new or old patient and display dynamic input fields.
     */
    private PatientRequestDTO patientRequestDTO;

    @Future
    @JsonFormat(pattern="yyyy-MM-dd", shape=JsonFormat.Shape.STRING)
    private LocalDate appointmentDate;

    private AppointmentStatus appointmentStatus;

    @NotNull
    private Long scheduleId;

    @NotNull
    private String creator; // logged in username or email

    @Builder
    public AppointmentRequestDTO(Long id, Long doctorId, PatientRequestDTO patientRequestDTO, LocalDate appointmentDate, AppointmentStatus appointmentStatus, Long scheduleId, String creator) {
        super(id);
        this.doctorId = doctorId;
        this.patientRequestDTO = patientRequestDTO;
        this.appointmentDate = appointmentDate;
        this.appointmentStatus = appointmentStatus;
        this.scheduleId = scheduleId;
        this.creator = creator;
    }
}
