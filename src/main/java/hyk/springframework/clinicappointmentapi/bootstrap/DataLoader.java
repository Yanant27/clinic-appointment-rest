package hyk.springframework.clinicappointmentapi.bootstrap;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import hyk.springframework.clinicappointmentapi.enums.DoctorStatus;
import hyk.springframework.clinicappointmentapi.repository.AppointmentRepository;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.repository.PatientRepository;
import hyk.springframework.clinicappointmentapi.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
@Slf4j
@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ScheduleRepository scheduleRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }

    private void loadData() {

        Doctor doctor1 = Doctor.builder()
                .name("Dr. Lin Htet")
                .phoneNumber("09111111111")
                .address("No.1, 1st street, Than Lyin")
                .degree("MBBS")
                .specialization("Internal medicine")
                .build();

        Doctor doctor2 = Doctor.builder()
                .name("Dr. Nyi Htut")
                .phoneNumber("09111111111")
                .address("No.2, 2nd street, Hlaing Township, Yangon")
                .degree("MBBS")
                .specialization("Cardiology")
                .build();

        Doctor doctor3 = Doctor.builder()
                .name("Dr. Shine Min")
                .phoneNumber("09111111111")
                .address("No.3, 3rd street, Lan Ma Taw Township, Yangon")
                .degree("MBBS")
                .specialization("General Surgery")
                .build();

        Doctor doctor4 = Doctor.builder()
                .name("Dr. Min Khant")
                .phoneNumber("09111111111")
                .address("No.4, 4th street, Tar Mwe Township, Yangon")
                .degree("MBBS")
                .specialization("General Surgery")
                .build();

        Doctor doctor5 = Doctor.builder()
                .name("Dr. Kyaw Swar")
                .phoneNumber("09111111111")
                .address("No.5, 5th street, Yan Kin Township, Yangon")
                .degree("MBBS")
                .specialization("Internal medicine")
                .build();

        Doctor doctor6 = Doctor.builder()
                .name("Dr. Nay Oo")
                .phoneNumber("09111111111")
                .address("No.6, 6th street, San Chaung Township, Yangon")
                .degree("MBBS")
                .specialization("Cardiology")
                .build();

        Doctor doctor7 = Doctor.builder()
                .name("Dr. Aung Ko")
                .phoneNumber("09111111111")
                .address("No.7, 7th street, Tha Mine Township, Yangon")
                .degree("MBBS")
                .specialization("Internal medicine")
                .build();

        Schedule schedule1 = Schedule.builder()
                .date(LocalDate.of(2022, 7, 7))
                .startTime(LocalTime.of(8,0))
                .endTime(LocalTime.of(10, 0))
                .status(DoctorStatus.AVAILABLE)
                .build();

        Schedule schedule2 = Schedule.builder()
                .date(LocalDate.of(2022, 7, 7))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .status(DoctorStatus.AVAILABLE)
                .build();

        Schedule schedule3 = Schedule.builder()
                .date(LocalDate.of(2022, 7, 7))
                .startTime(LocalTime.of(13, 0))
                .endTime(LocalTime.of(15, 0))
                .status(DoctorStatus.AVAILABLE)
                .build();

        Schedule schedule4 = Schedule.builder()
                .date(LocalDate.of(2022, 7, 8))
                .startTime(LocalTime.of(15, 0))
                .endTime(LocalTime.of(17, 30))
                .status(DoctorStatus.AVAILABLE)
                .build();

        Schedule schedule5 = Schedule.builder()
                        .date(LocalDate.of(2022, 7, 8))
                        .startTime(LocalTime.of(8,0))
                        .endTime(LocalTime.of(10, 0))
                        .status(DoctorStatus.AVAILABLE)
                        .build();

        Schedule schedule6 = Schedule.builder()
                        .date(LocalDate.of(2022, 7, 8))
                        .startTime(LocalTime.of(10, 0))
                        .endTime(LocalTime.of(12, 0))
                        .status(DoctorStatus.AVAILABLE)
                        .build();

        Schedule schedule7 = Schedule.builder()
                .date(LocalDate.of(2022, 7, 8))
                .startTime(LocalTime.of(13, 0))
                .endTime(LocalTime.of(15, 0))
                .status(DoctorStatus.AVAILABLE)
                .build();

        Schedule schedule8 = Schedule.builder()
                        .date(LocalDate.of(2022, 7, 8))
                        .startTime(LocalTime.of(15, 0))
                        .endTime(LocalTime.of(17, 30))
                        .status(DoctorStatus.AVAILABLE)
                        .build();

        doctor1.addSchedule(schedule1);
        doctor1.addSchedule(schedule8);
        doctor2.addSchedule(schedule2);
        doctor3.addSchedule(schedule3);
        doctor4.addSchedule(schedule4);
        doctor5.addSchedule(schedule5);
        doctor6.addSchedule(schedule6);
        doctor7.addSchedule(schedule7);

        doctorRepository.saveAll(List.of(doctor1, doctor2, doctor3, doctor4, doctor5, doctor6, doctor7));

        Patient patient1 = patientRepository.save(
                Patient.builder()
                .name("Hsu Hsu")
                .phoneNumber("09222222222")
                .address("No.30, Bo Ta Htaung Township, Yangon")
                .build());

        Patient patient2 = patientRepository.save(
                Patient.builder()
                .name("Aung Aung")
                .phoneNumber("09222222222")
                .address("No.30, Bo Ta Htaung Township, Yangon")
                .build());

        Patient patient3 = patientRepository.save(
                Patient.builder()
                .name("Kyaw Kyaw")
                .phoneNumber("09222222222")
                .address("No.30, Bo Ta Htaung Township, Yangon")
                .build());

        Patient patient4 = patientRepository.save(
                Patient.builder()
                .name("Phyu Phyu")
                .phoneNumber("09222222222")
                .address("No.30, Bo Ta Htaung Township, Yangon")
                .build());

        Patient patient5 = patientRepository.save(
                Patient.builder()
                .name("Su su")
                .phoneNumber("09222222222")
                .address("No.30, Bo Ta Htaung Township, Yangon")
                .build());

        Patient patient6 = patientRepository.save(
                Patient.builder()
                .name("Mee Mee")
                .phoneNumber("09222222222")
                .address("No.30, Bo Ta Htaung Township, Yangon")
                .build());

        Patient patient7 = patientRepository.save(
                Patient.builder()
                .name("Mya Mya")
                .phoneNumber("09222222222")
                .address("No.30, Bo Ta Htaung Township, Yangon")
                .build());

        appointmentRepository.save(
                Appointment.builder()
                .appointmentDate(schedule1.getDate())
                .appointmentStatus(AppointmentStatus.BOOKED)
                .schedule(schedule1)
                .doctor(doctor1)
                .patient(patient1)
                .build());

        appointmentRepository.save(
                Appointment.builder()
                        .appointmentDate(schedule8.getDate())
                        .appointmentStatus(AppointmentStatus.BOOKED)
                        .schedule(schedule8)
                        .doctor(doctor1)
                        .patient(patient1)
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .appointmentDate(schedule2.getDate())
                        .appointmentStatus(AppointmentStatus.BOOKED)
                        .schedule(schedule2)
                        .doctor(doctor2)
                        .patient(patient2)
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .appointmentDate(schedule3.getDate())
                        .appointmentStatus(AppointmentStatus.BOOKED)
                        .schedule(schedule3)
                        .doctor(doctor3)
                        .patient(patient3)
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .appointmentDate(schedule4.getDate())
                        .appointmentStatus(AppointmentStatus.BOOKED)
                        .schedule(schedule4)
                        .doctor(doctor4)
                        .patient(patient4)
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .appointmentDate(schedule5.getDate())
                        .appointmentStatus(AppointmentStatus.BOOKED)
                        .schedule(schedule5)
                        .doctor(doctor5)
                        .patient(patient5)
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .appointmentDate(schedule6.getDate())
                        .appointmentStatus(AppointmentStatus.BOOKED)
                        .schedule(schedule6)
                        .doctor(doctor6)
                        .patient(patient6)
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .appointmentDate(schedule7.getDate())
                        .appointmentStatus(AppointmentStatus.BOOKED)
                        .schedule(schedule7)
                        .doctor(doctor7)
                        .patient(patient7)
                        .build());

        log.debug("Data loaded");
//        System.out.println("No of doctor: " + doctorRepository.count());
//        System.out.println("No of patient: " + patientRepository.count());
//        System.out.println("No of Schedule: " + scheduleRepository.count());
//        System.out.println("No of appointment: " + appointmentRepository.count());

//        System.out.println("After deleting ...");
//        doctorRepository.deleteById(doctorRepository.findAll().get(0).getId());
//        appointmentRepository.deleteAll();
//        scheduleRepository.deleteById(scheduleRepository.findAll().get(0).getId());
//        patientRepository.deleteById(patientRepository.findAll().get(0).getId());

//        System.out.println("No of doctor: " + doctorRepository.count());
//        System.out.println("No of patient: " + patientRepository.count());
//        System.out.println("No of Schedule: " + scheduleRepository.count());
//        System.out.println("No of appointment: " + appointmentRepository.count());
    }
}
