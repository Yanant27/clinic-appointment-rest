package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.repository.PatientRepository;
import hyk.springframework.clinicappointmentapi.web.dto.PatientDTO;
import hyk.springframework.clinicappointmentapi.web.exception.NotFoundException;
import hyk.springframework.clinicappointmentapi.web.mapper.PatientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
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
    public List<PatientDTO> findAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::patientToPatientDto)
                .collect(Collectors.toList());
    }

    @Override
    public PatientDTO findPatientById(Long patientId) {
        Optional<Patient> optionalPatient = patientRepository.findById(patientId);
        if (optionalPatient.isPresent()) {
            return patientMapper.patientToPatientDto(optionalPatient.get());
        } else {
            throw new NotFoundException("Patient Not Found. ID: " + patientId);
        }
    }

    @Override
    public PatientDTO saveNewPatient(PatientDTO patientDTO) {
        return patientMapper.patientToPatientDto(
                patientRepository.save(patientMapper.patientDtoToPatient(patientDTO)));
    }

    @Override
    public PatientDTO updatePatient(Long patientId, PatientDTO patientDTO) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> {
            throw new NotFoundException("Patient Not Found. ID: " + patientId);
        });
        patient.setName(patientDTO.getName());
        patient.setAddress(patientDTO.getAddress());
        patient.setPhoneNumber(patientDTO.getPhoneNumber());
        return patientMapper.patientToPatientDto(
                patientRepository.save(patient));
    }

    @Override
    public void deletePatientById(Long patientId) {
        patientRepository.findById(patientId).ifPresentOrElse(appointment -> {
            patientRepository.deleteById(patientId);
        }, () -> {
            throw new NotFoundException("Patient Not Found. ID: " + patientId);
        });
    }
}
