package hyk.springframework.clinicappointmentapi.web.dto;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDTO {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
//    private List<AppointmentDTO> appointments;
}
