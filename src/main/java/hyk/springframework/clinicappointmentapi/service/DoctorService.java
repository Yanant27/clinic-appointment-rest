package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.web.dto.DoctorDTO;

import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
public interface DoctorService {
    List<DoctorDTO> findAllDoctors();

    DoctorDTO findDoctorById(Long doctorId);

    DoctorDTO saveNewDoctor(DoctorDTO doctorDTO);

    DoctorDTO updateDoctor(Long doctorId, DoctorDTO doctorDTO);

    void deleteDoctorById(Long doctorId);
}
