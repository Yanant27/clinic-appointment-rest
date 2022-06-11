package hyk.springframework.clinicappointmentapi.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hyk.springframework.clinicappointmentapi.enums.DoctorStatus;
import hyk.springframework.clinicappointmentapi.validation.ValidStartEndTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidStartEndTime
public class ScheduleDTO {
    private Long id;

    @NotNull
    @Future
    @JsonFormat(pattern="yyyy-MM-dd", shape=JsonFormat.Shape.STRING)
    private LocalDate date;

    @NotNull
    @JsonFormat(pattern="HH:mm", shape=JsonFormat.Shape.STRING)
    private LocalTime startTime;

    @NotNull
    @JsonFormat(pattern="HH:mm", shape=JsonFormat.Shape.STRING)
    private LocalTime endTime;

    @NotNull
    private Long doctorId;

    @NotEmpty
    private String doctorName;

    @NotNull
    private DoctorStatus doctorStatus;
}
