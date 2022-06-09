package hyk.springframework.clinicappointmentapi.web.mapper;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import hyk.springframework.clinicappointmentapi.repository.AppointmentRepository;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.repository.PatientRepository;
import hyk.springframework.clinicappointmentapi.repository.ScheduleRepository;
import hyk.springframework.clinicappointmentapi.web.dto.AppointmentDTO;
import hyk.springframework.clinicappointmentapi.web.dto.DoctorDTO;
import hyk.springframework.clinicappointmentapi.web.dto.PatientDTO;
import hyk.springframework.clinicappointmentapi.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Htoo Yanant Khin
 **/
public abstract class AppointmentMapperDecorator implements AppointmentMapper {
    private AppointmentRepository appointmentRepository;
    private ScheduleRepository scheduleRepository;
    private DoctorRepository doctorRepository;;
    private PatientRepository patientRepository;
    private AppointmentMapper appointmentMapper;

    @Autowired
    public void setAppointmentRepository(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Autowired
    public void setScheduleRepository(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Autowired
    public void setDoctorRepository(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Autowired
    public void setPatientRepository(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Autowired
    @Qualifier("delegate")
    public void setAppointmentMapper(AppointmentMapper appointmentMapper) {
        this.appointmentMapper = appointmentMapper;
    }

    @Override
    public AppointmentDTO appointmentToAppointmentDto(Appointment appointment) {
        AppointmentDTO appointmentDTO = appointmentMapper.appointmentToAppointmentDto(appointment);
        appointmentDTO.setAppointmentId(appointment.getId());
        appointmentDTO.setAppointmentStatus(appointment.getAppointmentStatus().name());
        appointmentDTO.setAppointmentDate(appointment.getSchedule().getDate());
        appointmentDTO.setScheduleId(appointment.getSchedule().getId());
        appointmentDTO.setStartTime(appointment.getSchedule().getStartTime());
        appointmentDTO.setEndTime(appointment.getSchedule().getEndTime());
        appointmentDTO.setDoctorDTO(DoctorDTO.builder()
                        .id(appointment.getDoctor().getId())
                        .name(appointment.getDoctor().getName())
                        .degree(appointment.getDoctor().getDegree())
                        .specialization(appointment.getDoctor().getSpecialization())
                        .address(appointment.getDoctor().getAddress())
                        .phoneNumber(appointment.getDoctor().getPhoneNumber())
                .build());
        appointmentDTO.setPatientDTO(PatientDTO.builder()
                        .id(appointment.getPatient().getId())
                        .name(appointment.getPatient().getName())
                        .address(appointment.getPatient().getAddress())
                        .phoneNumber(appointment.getPatient().getPhoneNumber())
                        .build());
        appointmentDTO.getDoctorDTO().setId(appointment.getDoctor().getId());

        return appointmentDTO;
    }

    @Override
    public Appointment appointmentDtoToAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment;
        if (appointmentDTO.getAppointmentId() != null) {
            appointment = appointmentRepository.findById(appointmentDTO.getAppointmentId()).orElse(new Appointment());
        } else {
            appointment = appointmentMapper.appointmentDtoToAppointment(appointmentDTO);
            appointment.setDoctor(doctorRepository.findById(appointmentDTO.getDoctorDTO().getId()).orElseThrow(NotFoundException::new));
            appointment.setPatient(patientRepository.findById(appointmentDTO.getPatientDTO().getId()).orElseThrow(NotFoundException::new));
            appointment.setSchedule(scheduleRepository.findById(appointmentDTO.getScheduleId()).orElseThrow(NotFoundException::new));
        }
        appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
        appointment.setAppointmentStatus(AppointmentStatus.valueOf(appointmentDTO.getAppointmentStatus()));
        return appointment;
    }
}
