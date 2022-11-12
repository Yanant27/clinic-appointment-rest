package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorResponseDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Htoo Yanant Khin
 **/
@Mapper
@DecoratedWith(DoctorMapperDecorator.class)
public interface DoctorMapper {
    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    // Create or update doctor
    Doctor doctorRequestDtoToDoctor(DoctorRequestDTO doctorRequestDTO);

    // Retrieve doctor by Id
    DoctorResponseDTO doctorToDoctorResponseDto(Doctor doctor);
}
