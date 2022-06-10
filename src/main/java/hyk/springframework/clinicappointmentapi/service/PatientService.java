package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.web.dto.PatientDTO;

import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
public interface PatientService {
    List<PatientDTO> findAllPatients();

    PatientDTO findPatientById(Long patientId);

    PatientDTO saveNewPatient(PatientDTO patientDTO);

    PatientDTO updatePatient(Long patientId, PatientDTO patientDTO);

    void deletePatientById(Long PatientId);
}
