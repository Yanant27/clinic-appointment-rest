package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.dto.patient.PatientRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientUpdateDTO;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientResponseDTO;

import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
public interface PatientService {
    List<PatientResponseDTO> findAllPatients();

    PatientResponseDTO findPatientById(Long patientId);

    PatientResponseDTO saveNewPatient(PatientRegistrationDTO patientRegistrationDTO);

    PatientResponseDTO updatePatient(Long patientId, PatientUpdateDTO patientUpdateDTO);

    void deletePatientById(Long patientId);
}
