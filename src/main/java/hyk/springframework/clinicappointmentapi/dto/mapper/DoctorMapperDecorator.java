package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorResponseDTO;
import hyk.springframework.clinicappointmentapi.exception.NotFoundException;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.stream.Collectors;

/**
 * @author Htoo Yanant Khin
 **/
public class DoctorMapperDecorator implements DoctorMapper {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
//    @Qualifier("delegate")
    private AppointmentMapper appointmentMapper;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    @Qualifier("delegate")
    private DoctorMapper doctorMapper;

    @Override
    public Doctor doctorRequestDtoToDoctor(DoctorRequestDTO doctorRequestDTO) {
        if (doctorRequestDTO == null) {
            return null;
        }
        Doctor doctor;
        if (doctorRequestDTO.getId() != null) {
            // Update existing doctor case
            doctor = doctorRepository.findById(doctorRequestDTO.getId())
                    .orElseThrow(() -> {
                        throw new NotFoundException("Doctor Not Found. ID: " + doctorRequestDTO.getId());
                    });
        } else {
            // Register new doctor case
            doctor = doctorMapper.doctorRequestDtoToDoctor(doctorRequestDTO);
        }
        // Update data
        doctor.setName(doctorRequestDTO.getName());
        doctor.setAge(doctorRequestDTO.getAge());
        doctor.setDateOfBirth(doctorRequestDTO.getDateOfBirth());
        doctor.setGender(doctorRequestDTO.getGender());
        doctor.setAddress(doctorRequestDTO.getAddress());
        doctor.setPhoneNumber(doctorRequestDTO.getPhoneNumber());
        doctor.setQualifications(doctorRequestDTO.getQualifications());
        doctor.setSpecialization(doctorRequestDTO.getSpecialization());
        return doctor;
    }

    @Override
    public DoctorResponseDTO doctorToDoctorResponseDto(Doctor doctor) {
        if (doctor == null) {
            return null;
        }
        DoctorResponseDTO doctorResponseDTO = doctorMapper.doctorToDoctorResponseDto(doctor);
        doctorResponseDTO.setId(doctor.getId());
        doctorResponseDTO.setName(doctor.getName());
        doctorResponseDTO.setAge(doctor.getAge());
        doctorResponseDTO.setDateOfBirth(doctor.getDateOfBirth());
        doctorResponseDTO.setGender(doctor.getGender());
        doctorResponseDTO.setAddress(doctor.getAddress());
        doctorResponseDTO.setPhoneNumber(doctor.getPhoneNumber());
        doctorResponseDTO.setQualifications(doctor.getQualifications());
        doctorResponseDTO.setSpecialization(doctor.getSpecialization());
        if (doctor.getSchedules() != null) {
            doctorResponseDTO.setScheduleResponseDTOS(doctor.getSchedules().stream()
                    .map(schedule -> scheduleMapper.scheduleToScheduleResponseDto(schedule))
                    .collect(Collectors.toList()));
        }
        if (doctor.getAppointments() != null) {
            doctorResponseDTO.setAppointmentResponseDTOS(doctor.getAppointments().stream()
                    .map(appointment -> appointmentMapper.appointmentToAppointmentResponseDto(appointment))
                    .collect(Collectors.toList()));
        }
        return doctorResponseDTO;
    }
}
