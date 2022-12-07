package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.stream.Collectors;

/**
 * @author Htoo Yanant Khin
 **/
public abstract class DoctorMapperDecorator implements DoctorMapper {
    @Autowired
//    @Qualifier("delegate")
    private AppointmentMapper appointmentMapper;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    @Qualifier("delegate")
    private DoctorMapper doctorMapper;

    @Override
    public DoctorResponseDTO doctorToDoctorResponseDto(Doctor doctor) {
        DoctorResponseDTO doctorResponseDTO = doctorMapper.doctorToDoctorResponseDto(doctor);
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
