package hyk.springframework.clinicappointmentapi.dto.patient;

import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentResponseDTO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class PatientResponseDTO extends PatientUpdateDTO {
    @Builder.Default
    private List<AppointmentResponseDTO> appointments  = new ArrayList<>();

    private String createdBy;

    private String modifiedBy;
}
