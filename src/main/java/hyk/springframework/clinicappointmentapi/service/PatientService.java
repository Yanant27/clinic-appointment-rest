package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.dto.patient.PatientRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientResponseDTO;

import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
public interface PatientService {
    List<PatientResponseDTO> findAllPatients();

    PatientResponseDTO findPatientById(Long patientId);

    PatientResponseDTO saveNewPatient(PatientRequestDTO patientRequestDTO);

    PatientResponseDTO updatePatient(Long patientId, PatientRequestDTO patientRequestDTO);

    void deletePatientById(Long PatientId);
}
