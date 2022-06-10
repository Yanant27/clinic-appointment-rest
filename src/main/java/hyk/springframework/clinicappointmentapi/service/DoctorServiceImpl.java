package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.web.dto.DoctorDTO;
import hyk.springframework.clinicappointmentapi.web.mapper.DoctorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Htoo Yanant Khin
 **/
@RequiredArgsConstructor
@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Override
    public List<DoctorDTO> findAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::doctorToDoctorDTto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorDTO findDoctorById(Long doctorId) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        if (optionalDoctor.isPresent()) {
            return doctorMapper.doctorToDoctorDTto(optionalDoctor.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor Not Found. ID: " + doctorId);
        }
    }

    @Override
    @Transactional
    public DoctorDTO saveNewDoctor(DoctorDTO doctorDTO) {
        return doctorMapper.doctorToDoctorDTto(
                doctorRepository.save(doctorMapper.doctorDtoToDoctor(doctorDTO)));
    }

    @Override
    @Transactional
    public DoctorDTO updateDoctor(Long doctorId, DoctorDTO doctorDTO) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor Not Found. ID: " + doctorId);
        });
        doctor.setName(doctorDTO.getName());
        doctor.setAddress(doctorDTO.getAddress());
        doctor.setPhoneNumber(doctorDTO.getPhoneNumber());
        doctor.setDegree(doctorDTO.getDegree());
        doctor.setSpecialization(doctorDTO.getSpecialization());
        return doctorMapper.doctorToDoctorDTto(
                doctorRepository.save(doctor));
    }

    @Override
    @Transactional
    public void deleteDoctorById(Long doctorId) {
        doctorRepository.findById(doctorId).ifPresentOrElse(appointment -> {
            doctorRepository.deleteById(doctorId);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor Not Found. ID: " + doctorId);
        });
    }
}
