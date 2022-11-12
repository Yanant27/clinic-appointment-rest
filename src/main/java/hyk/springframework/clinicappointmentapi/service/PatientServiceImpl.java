package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.dto.mapper.PatientMapper;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientResponseDTO;
import hyk.springframework.clinicappointmentapi.exception.NotFoundException;
import hyk.springframework.clinicappointmentapi.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Htoo Yanant Khin
 **/
@RequiredArgsConstructor
@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public List<PatientResponseDTO> findAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::patientToPatientResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public PatientResponseDTO findPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .map(patientMapper::patientToPatientResponseDto)
                .orElseThrow(() -> {
                    throw new NotFoundException("Patient Not Found. ID: " + patientId);
                });
    }

    @Override
    public PatientResponseDTO saveNewPatient(PatientRequestDTO patientRequestDTO) {
        return patientMapper.patientToPatientResponseDto(
                patientRepository.save(patientMapper.patientRequestDtoToPatient(patientRequestDTO)));
    }

    @Override
    public PatientResponseDTO updatePatient(Long patientId, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> {
            throw new NotFoundException("Patient Not Found. ID: " + patientId);
        });
        patient.setName(patientRequestDTO.getName());
        patient.setAge(patientRequestDTO.getAge());
        patient.setGender(patientRequestDTO.getGender());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setPhoneNumber(patientRequestDTO.getPhoneNumber());
        return patientMapper.patientToPatientResponseDto(patientRepository.save(patient));
    }

    @Override
    public void deletePatientById(Long patientId) {
        patientRepository.findById(patientId)
                .ifPresentOrElse(patient ->
                        patientRepository.deleteById(patientId), () -> {
                    throw new NotFoundException("Patient Not Found. ID: " + patientId);
                });
    }
}
