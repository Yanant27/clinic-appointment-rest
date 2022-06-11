package hyk.springframework.clinicappointmentapi.web.dto;

import hyk.springframework.clinicappointmentapi.validation.ValidPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDTO {
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String address;

    @NotEmpty
    @ValidPhoneNumber
    private String phoneNumber;

    @NotEmpty
    private String degree;

    private String specialization;
}
