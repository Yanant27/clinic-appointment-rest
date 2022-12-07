package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.domain.security.Role;
import hyk.springframework.clinicappointmentapi.domain.security.User;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorUpdateDTO;
import hyk.springframework.clinicappointmentapi.dto.mapper.DoctorMapper;
import hyk.springframework.clinicappointmentapi.exception.ResourceAlreadyExistException;
import hyk.springframework.clinicappointmentapi.exception.ResourceNotFoundException;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.repository.security.RoleRepository;
import hyk.springframework.clinicappointmentapi.repository.security.UserRepository;
import hyk.springframework.clinicappointmentapi.util.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Htoo Yanant Khin
 **/
@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final DoctorMapper doctorMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<DoctorResponseDTO> findAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::doctorToDoctorResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<DoctorResponseDTO> findAllDoctorsBySpecialization(String specialization) {
        return doctorRepository.findAllBySpecializationEqualsIgnoreCase(specialization).stream()
                .map(doctorMapper::doctorToDoctorResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponseDTO findDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .map(doctorMapper::doctorToDoctorResponseDto)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("Doctor Not Found. ID: " + doctorId);
                });
    }

    @Override
    @Transactional
    public DoctorResponseDTO saveNewDoctor(DoctorRegistrationDTO doctorRegistrationDTO) {
        if (userRepository.findByUsername(doctorRegistrationDTO.getUsername()).isPresent()) {
            throw new ResourceAlreadyExistException("Username already exists !");
        }
        // Create new user with role "DOCTOR"
        Role role = roleRepository.findByNameEqualsIgnoreCase("DOCTOR")
                .orElseThrow(() -> new ResourceNotFoundException("Role Not Found"));
        User savedUser = User.builder()
                .username(doctorRegistrationDTO.getUsername())
                .password(passwordEncoder.encode(doctorRegistrationDTO.getPassword()))
                .roles(Set.of(role)).build();
        Doctor doctor = doctorMapper.doctorRegistrationDtoToDoctor(doctorRegistrationDTO);
        doctor.setUser(savedUser);
        doctor.setCreatedBy(LoginUserUtil.getLoginUsername());
        doctor.setModifiedBy(LoginUserUtil.getLoginUsername());
        return doctorMapper.doctorToDoctorResponseDto(doctorRepository.save(doctor));
    }

    @Override
    @Transactional
    public DoctorResponseDTO updateDoctor(Long doctorId, DoctorUpdateDTO doctorUpdateDTO) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Doctor Not Found. ID: " + doctorId);
        });
        doctor.setName(doctorUpdateDTO.getName());
        doctor.setGender(doctorUpdateDTO.getGender());
        doctor.setAddress(doctorUpdateDTO.getAddress());
        doctor.setPhoneNumber(doctorUpdateDTO.getPhoneNumber());
        doctor.setQualifications(doctorUpdateDTO.getQualifications());
        doctor.setSpecialization(doctorUpdateDTO.getSpecialization());
        doctor.setModifiedBy(LoginUserUtil.getLoginUsername());
        return doctorMapper.doctorToDoctorResponseDto(doctorRepository.save(doctor));
    }

    @Override
    @Transactional
    public void deleteDoctorById(Long doctorId) {
        doctorRepository.findById(doctorId)
                .ifPresentOrElse(appointment -> {
                    doctorRepository.deleteById(doctorId);
                }, () -> {
                    throw new ResourceNotFoundException("Doctor Not Found. ID: " + doctorId);
                });
    }
}
