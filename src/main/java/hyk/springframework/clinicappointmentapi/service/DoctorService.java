package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorResponseDTO;

import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
public interface DoctorService {
    List<DoctorResponseDTO> findAllDoctors();

    DoctorResponseDTO findDoctorById(Long doctorId);

    DoctorResponseDTO saveNewDoctor(DoctorRequestDTO doctorRequestDTO);

    DoctorResponseDTO updateDoctor(Long doctorId, DoctorRequestDTO doctorRequestDTO);

    void deleteDoctorById(Long doctorId);

    List<DoctorResponseDTO> findAllDoctorsBySpecialization(String specialization);
}
