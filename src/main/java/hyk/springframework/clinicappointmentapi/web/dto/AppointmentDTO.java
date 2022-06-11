package hyk.springframework.clinicappointmentapi.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDTO {
    private Long appointmentId;

    @JsonFormat(pattern="yyyy-MM-dd", shape=JsonFormat.Shape.STRING)
    private LocalDate appointmentDate;

    private AppointmentStatus appointmentStatus;
    private Long scheduleId;

    @JsonFormat(pattern="HH:mm", shape=JsonFormat.Shape.STRING)
    private LocalTime startTime;

    @JsonFormat(pattern="HH:mm", shape=JsonFormat.Shape.STRING)
    private LocalTime endTime;

    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
}
