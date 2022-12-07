package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.domain.security.Role;
import hyk.springframework.clinicappointmentapi.domain.security.User;
import hyk.springframework.clinicappointmentapi.dto.mapper.PatientMapper;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientUpdateDTO;
import hyk.springframework.clinicappointmentapi.exception.ResourceAlreadyExistException;
import hyk.springframework.clinicappointmentapi.exception.ResourceNotFoundException;
import hyk.springframework.clinicappointmentapi.repository.PatientRepository;
import hyk.springframework.clinicappointmentapi.repository.security.RoleRepository;
import hyk.springframework.clinicappointmentapi.repository.security.UserRepository;
import hyk.springframework.clinicappointmentapi.util.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PatientMapper patientMapper;

    private final PasswordEncoder passwordEncoder;

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
                    throw new ResourceNotFoundException("Patient Not Found. ID: " + patientId);
                });
    }

    @Override
    public PatientResponseDTO saveNewPatient(PatientRegistrationDTO patientRegistrationDTO) {
        if (userRepository.findByUsername(patientRegistrationDTO.getUsername()).isPresent()) {
            throw new ResourceAlreadyExistException("Username already exists !");
        }
        // Create new user with role "PATIENT"
        Role role = roleRepository.findByNameEqualsIgnoreCase("PATIENT")
                .orElseThrow(() -> new ResourceNotFoundException("Role Not Found"));
        User savedUser = User.builder()
                .username(patientRegistrationDTO.getUsername())
                .password(passwordEncoder.encode(patientRegistrationDTO.getPassword()))
                .role(role).build();
        Patient patient = patientMapper.patientRegistrationDtoToPatient(patientRegistrationDTO);
        patient.setUser(savedUser);
        patient.setCreatedBy(LoginUserUtil.getLoginUsername().equals("admin") ? "admin" : patientRegistrationDTO.getUsername());
        patient.setModifiedBy(LoginUserUtil.getLoginUsername().equals("admin") ? "admin" : patientRegistrationDTO.getUsername());
        return patientMapper.patientToPatientResponseDto(patientRepository.save(patient));
    }

    @Override
    public PatientResponseDTO updatePatient(Long patientId, PatientUpdateDTO patientUpdateDTO) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Patient Not Found. ID: " + patientId);
        });
        patient.setName(patientUpdateDTO.getName());
        patient.setGender(patientUpdateDTO.getGender());
        patient.setAddress(patientUpdateDTO.getAddress());
        patient.setPhoneNumber(patientUpdateDTO.getPhoneNumber());
        patient.setModifiedBy(LoginUserUtil.getLoginUsername());
        return patientMapper.patientToPatientResponseDto(patientRepository.save(patient));
    }

    @Override
    public void deletePatientById(Long patientId) {
        patientRepository.findById(patientId)
                .ifPresentOrElse(patient ->
                        patientRepository.deleteById(patientId), () -> {
                    throw new ResourceNotFoundException("Patient Not Found. ID: " + patientId);
                });
    }
}
