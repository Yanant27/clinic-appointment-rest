package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorResponseDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

/**
 * @author Htoo Yanant Khin
 **/
@Mapper
@DecoratedWith(DoctorMapperDecorator.class)
public interface DoctorMapper {
    // Retrieve doctor by Id
    DoctorResponseDTO doctorToDoctorResponseDto(Doctor doctor);

    // Register new doctor
    Doctor doctorRegistrationDtoToDoctor(DoctorRegistrationDTO doctorRegistrationDTO);
}
