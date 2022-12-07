package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.stream.Collectors;

/**
 * @author Htoo Yanant Khin
 **/
public abstract class PatientMapperDecorator implements PatientMapper {
    @Autowired
//    @Qualifier("delegate")
    private AppointmentMapper appointmentMapper;

    @Autowired
    @Qualifier("delegate")
    private PatientMapper patientMapper;

    @Override
    public PatientResponseDTO patientToPatientResponseDto(Patient patient) {
        PatientResponseDTO patientResponseDTO = patientMapper.patientToPatientResponseDto(patient);
        if (patient.getAppointments() != null) {
            patientResponseDTO.setAppointments(patient.getAppointments().stream()
                    .map(appointment -> appointmentMapper.appointmentToAppointmentResponseDto(appointment))
                    .collect(Collectors.toList()));
        }
        return patientResponseDTO;
    }
}
