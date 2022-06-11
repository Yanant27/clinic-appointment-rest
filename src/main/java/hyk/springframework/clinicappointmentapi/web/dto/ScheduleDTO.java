package hyk.springframework.clinicappointmentapi.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hyk.springframework.clinicappointmentapi.enums.DoctorStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDTO {
    private Long id;

    @JsonFormat(pattern="yyyy-MM-dd", shape=JsonFormat.Shape.STRING)
    private LocalDate date;

    @JsonFormat(pattern="HH:mm", shape=JsonFormat.Shape.STRING)
    private LocalTime startTime;

    @JsonFormat(pattern="HH:mm", shape=JsonFormat.Shape.STRING)
    private LocalTime endTime;

    private Long doctorId;
    private String doctorName;
    private DoctorStatus doctorStatus;
}
