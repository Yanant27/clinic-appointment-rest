package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorUpdateDTO;

import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
public interface DoctorService {
    List<DoctorResponseDTO> findAllDoctors();

    DoctorResponseDTO findDoctorById(Long doctorId);

    DoctorResponseDTO saveNewDoctor(DoctorRegistrationDTO doctorRegistrationDTO);

    DoctorResponseDTO updateDoctor(Long doctorId, DoctorUpdateDTO doctorUpdateDTO);

    void deleteDoctorById(Long doctorId);

    List<DoctorResponseDTO> findAllDoctorsBySpecialization(String specialization);
}
