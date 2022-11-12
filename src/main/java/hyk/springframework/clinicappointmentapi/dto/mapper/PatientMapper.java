package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientResponseDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Htoo Yanant Khin
 **/
@Mapper
@DecoratedWith(PatientMapperDecorator.class)
public interface PatientMapper {
    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);

    // Create or update patient
    Patient patientRequestDtoToPatient(PatientRequestDTO patientRequestDTO);

    // Retrieve patient by Id
    PatientResponseDTO patientToPatientResponseDto(Patient patient);

    PatientRequestDTO patientToPatientRequestDto(Patient patient);
}
