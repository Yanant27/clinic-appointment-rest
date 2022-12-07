package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientResponseDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

/**
 * @author Htoo Yanant Khin
 **/
@Mapper
@DecoratedWith(PatientMapperDecorator.class)
public interface PatientMapper {
    // Retrieve patient by Id
    PatientResponseDTO patientToPatientResponseDto(Patient patient);

    // Register new patient
    Patient patientRegistrationDtoToPatient(PatientRegistrationDTO patientRegistrationDTO);
}
