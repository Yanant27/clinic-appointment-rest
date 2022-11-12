package hyk.springframework.clinicappointmentapi.dto.appointment;

import hyk.springframework.clinicappointmentapi.dto.BaseDTO;
import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class AppointmentUpdateStatusDTO extends BaseDTO {
    private AppointmentStatus appointmentStatus;

    public AppointmentUpdateStatusDTO(Long id, AppointmentStatus appointmentStatus) {
        super(id);
        this.appointmentStatus = appointmentStatus;
    }
}
