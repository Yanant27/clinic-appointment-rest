package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.mapper.DoctorMapper;
import hyk.springframework.clinicappointmentapi.exception.NotFoundException;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Htoo Yanant Khin
 **/
@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;

    private final ScheduleRepository scheduleRepository;

    private final DoctorMapper doctorMapper;

    @Override
    public List<DoctorResponseDTO> findAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::doctorToDoctorResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponseDTO findDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .map(doctorMapper::doctorToDoctorResponseDto)
                .orElseThrow(() -> {
                    throw new NotFoundException("Doctor Not Found. ID: " + doctorId);
                });
    }

    @Override
    @Transactional
    public DoctorResponseDTO saveNewDoctor(DoctorRequestDTO doctorRequestDTO) {
        return doctorMapper.doctorToDoctorResponseDto(
                doctorRepository.save(doctorMapper.doctorRequestDtoToDoctor(doctorRequestDTO)));
    }

    @Override
    @Transactional
    public DoctorResponseDTO updateDoctor(Long doctorId, DoctorRequestDTO doctorRequestDTO) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> {
            throw new NotFoundException("Doctor Not Found. ID: " + doctorId);
        });
        doctor.setName(doctorRequestDTO.getName());
        doctor.setAge(doctorRequestDTO.getAge());
        doctor.setGender(doctorRequestDTO.getGender());
        doctor.setAddress(doctorRequestDTO.getAddress());
        doctor.setPhoneNumber(doctorRequestDTO.getPhoneNumber());
        doctor.setQualifications(doctorRequestDTO.getQualifications());
        doctor.setSpecialization(doctorRequestDTO.getSpecialization());
        return doctorMapper.doctorToDoctorResponseDto(doctorRepository.save(doctor));
    }

    @Override
    @Transactional
    public void deleteDoctorById(Long doctorId) {
        doctorRepository.findById(doctorId)
                .ifPresentOrElse(appointment -> {
                    // Logically delete all schedules for doctor
                    scheduleRepository.findAllByDoctorId(doctorId)
                            .forEach(schedule -> schedule.setDoctor(null));
                    // Permanently delete doctor
                    doctorRepository.deleteById(doctorId);
                }, () -> {
                    throw new NotFoundException("Doctor Not Found. ID: " + doctorId);
                });
    }

    @Override
    public List<DoctorResponseDTO> findAllDoctorsBySpecialization(String specialization) {
        return doctorRepository.findAllBySpecialization(specialization).stream()
                .map(doctorMapper::doctorToDoctorResponseDto)
                .collect(Collectors.toList());
    }
}
