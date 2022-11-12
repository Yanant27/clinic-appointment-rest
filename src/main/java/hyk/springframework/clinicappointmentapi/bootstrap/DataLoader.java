package hyk.springframework.clinicappointmentapi.bootstrap;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.domain.security.Role;
import hyk.springframework.clinicappointmentapi.domain.security.User;
import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import hyk.springframework.clinicappointmentapi.enums.Gender;
import hyk.springframework.clinicappointmentapi.enums.ScheduleStatus;
import hyk.springframework.clinicappointmentapi.repository.AppointmentRepository;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.repository.PatientRepository;
import hyk.springframework.clinicappointmentapi.repository.ScheduleRepository;
import hyk.springframework.clinicappointmentapi.repository.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
@Slf4j
@RequiredArgsConstructor
@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final ScheduleRepository scheduleRepository;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;

    private final AppointmentRepository appointmentRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        loadSecurityData();
        loadData();
    }

    private void loadSecurityData() {
        userRepository.save(User.builder().username("admin").password(encoder.encode("admin")).role(Role.builder().name("ADMIN").build()).build());
        userRepository.save(User.builder().username("doctor").password(encoder.encode("doctor")).role(Role.builder().name("DOCTOR").build()).build());
        userRepository.save(User.builder().username("patient").password(encoder.encode("patient")).role(Role.builder().name("PATIENT").build()).build());
    }

    private void loadData() {
        Doctor doctor1 = Doctor.builder()
                .name("Dr. Lin Htet")
                .age(32L)
                .gender(Gender.MALE)
                .phoneNumber("09111111111")
                .address("No.1, 1st street, Than Lyin")
                .qualifications("MBBS")
                .specialization("Internal medicine")
                .build();
        Doctor doctor2 = Doctor.builder()
                .name("Dr. Nyi Htut")
                .age(35L)
                .gender(Gender.MALE)
                .phoneNumber("09111111111")
                .address("No.2, 2nd street, Hlaing Township, Yangon")
                .qualifications("MBBS")
                .specialization("Cardiology")
                .build();
        Doctor doctor3 = Doctor.builder()
                .name("Dr. Shine Min")
                .age(38L)
                .gender(Gender.MALE)
                .phoneNumber("09111111111")
                .address("No.3, 3rd street, Lan Ma Taw Township, Yangon")
                .qualifications("MBBS")
                .specialization("General Surgery")
                .build();
        Doctor doctor4 = Doctor.builder()
                .name("Dr. Min Khant")
                .age(35L)
                .gender(Gender.MALE)
                .phoneNumber("09111111111")
                .address("No.4, 4th street, Tar Mwe Township, Yangon")
                .qualifications("MBBS")
                .specialization("General Surgery")
                .build();
        Doctor doctor5 = Doctor.builder()
                .name("Dr. Kyaw Swar")
                .age(40L)
                .gender(Gender.MALE)
                .phoneNumber("09111111111")
                .address("No.5, 5th street, Yan Kin Township, Yangon")
                .qualifications("MBBS")
                .specialization("Internal medicine")
                .build();
        Doctor doctor6 = Doctor.builder()
                .name("Dr. Nay Oo")
                .age(39L)
                .gender(Gender.MALE)
                .phoneNumber("09111111111")
                .address("No.6, 6th street, San Chaung Township, Yangon")
                .qualifications("MBBS")
                .specialization("Cardiology")
                .build();
        Doctor doctor7 = Doctor.builder()
                .name("Dr. Aung Ko")
                .age(41L)
                .gender(Gender.MALE)
                .phoneNumber("09111111111")
                .address("No.7, 7th street, Tha Mine Township, Yangon")
                .qualifications("MBBS")
                .specialization("Internal medicine")
                .build();
        doctorRepository.saveAll(List.of(doctor1, doctor2, doctor3, doctor4, doctor5, doctor6, doctor7));

        Schedule schedule1 = Schedule.builder()
                .dayOfWeek(DayOfWeek.SUNDAY.name())
                .timeslot("09:00 ~ 10:00")
                .scheduleStatus(ScheduleStatus.OCCUPIED)
                .doctor(doctor1)
                .build();
        Schedule schedule2 = Schedule.builder()
                .dayOfWeek(DayOfWeek.MONDAY.name())
                .timeslot("09:00 ~ 10:00")
                .scheduleStatus(ScheduleStatus.OCCUPIED)
                .doctor(doctor1)
                .build();
        Schedule schedule3 = Schedule.builder()
                .dayOfWeek(DayOfWeek.MONDAY.name())
                .timeslot("10:00 ~ 11:00")
                .scheduleStatus(ScheduleStatus.OCCUPIED)
                .doctor(doctor2)
                .build();
        Schedule schedule4 = Schedule.builder()
                .dayOfWeek(DayOfWeek.THURSDAY.name())
                .timeslot("12:00 ~ 13:00")
                .scheduleStatus(ScheduleStatus.OCCUPIED)
                .doctor(doctor3)
                .build();
        Schedule schedule5 = Schedule.builder()
                .dayOfWeek(DayOfWeek.WEDNESDAY.name())
                .timeslot("13:00 ~ 14:00")
                .scheduleStatus(ScheduleStatus.OCCUPIED)
                .doctor(doctor4)
                .build();
        Schedule schedule6 = Schedule.builder()
                .dayOfWeek(DayOfWeek.TUESDAY.name())
                .timeslot("15:00 ~ 16:00")
                .scheduleStatus(ScheduleStatus.OCCUPIED)
                .doctor(doctor4)
                .build();
        Schedule schedule7 = Schedule.builder()
                .dayOfWeek(DayOfWeek.FRIDAY.name())
                .timeslot("17:00 ~ 18:00")
                .scheduleStatus(ScheduleStatus.OCCUPIED)
                .doctor(doctor5)
                .build();
        Schedule schedule8 = Schedule.builder()
                .dayOfWeek(DayOfWeek.SATURDAY.name())
                .timeslot("19:00 ~ 20:00")
                .scheduleStatus(ScheduleStatus.OCCUPIED)
                .doctor(doctor6)
                .build();
        Schedule schedule9 = Schedule.builder()
                .dayOfWeek(DayOfWeek.SUNDAY.name())
                .timeslot("12:00 ~ 13:00")
                .scheduleStatus(ScheduleStatus.OCCUPIED)
                .doctor(doctor7)
                .build();
        Schedule schedule10 = Schedule.builder()
                .dayOfWeek(DayOfWeek.SUNDAY.name())
                .timeslot("14:00 ~ 15:00")
                .build();
        Schedule schedule11 = Schedule.builder()
                .dayOfWeek(DayOfWeek.SUNDAY.name())
                .timeslot("15:00 ~ 16:00")
                .build();
        scheduleRepository.saveAll(List.of(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6,
                schedule7, schedule8, schedule9, schedule10, schedule11));

        Patient patient1 = patientRepository.save(
                Patient.builder()
                        .name("Hsu Hsu")
                        .age(24L)
                        .gender(Gender.FEMALE)
                        .phoneNumber("09222222222")
                        .address("No.30, Bo Ta Htaung Township, Yangon")
                        .build());
        Patient patient2 = patientRepository.save(
                Patient.builder()
                        .age(28L)
                        .gender(Gender.MALE)
                        .name("Aung Aung")
                        .phoneNumber("09222222222")
                        .address("No.30, Bo Ta Htaung Township, Yangon")
                        .build());
        Patient patient3 = patientRepository.save(
                Patient.builder()
                        .age(28L)
                        .gender(Gender.MALE)
                        .name("Kyaw Kyaw")
                        .phoneNumber("09222222222")
                        .address("No.30, Bo Ta Htaung Township, Yangon")
                        .build());
        Patient patient4 = patientRepository.save(
                Patient.builder()
                        .name("Phyu Phyu")
                        .age(29L)
                        .gender(Gender.FEMALE)
                        .phoneNumber("09222222222")
                        .address("No.30, Bo Ta Htaung Township, Yangon")
                        .build());
        Patient patient5 = patientRepository.save(
                Patient.builder()
                        .name("Su Su")
                        .age(31L)
                        .gender(Gender.FEMALE)
                        .phoneNumber("09222222222")
                        .address("No.30, Bo Ta Htaung Township, Yangon")
                        .build());
        Patient patient6 = patientRepository.save(
                Patient.builder()
                        .name("Mee Mee")
                        .age(34L)
                        .gender(Gender.FEMALE)
                        .phoneNumber("09222222222")
                        .address("No.30, Bo Ta Htaung Township, Yangon")
                        .build());
        Patient patient7 = patientRepository.save(
                Patient.builder()
                        .name("Mya Mya")
                        .age(35L)
                        .gender(Gender.FEMALE)
                        .phoneNumber("09222222222")
                        .address("No.30, Bo Ta Htaung Township, Yangon")
                        .build());
        patientRepository.saveAll(List.of(patient1, patient2, patient3, patient4, patient5, patient6, patient7));

        appointmentRepository.save(
                Appointment.builder()
                        .doctor(doctor1)
                        .patient(patient1)
                        .schedule(schedule1)
                        .appointmentDate(LocalDate.now().plusDays(3))
                        .appointmentStatus(AppointmentStatus.PENDING)
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .doctor(doctor1)
                        .patient(patient1)
                        .schedule(schedule2)
                        .appointmentDate(LocalDate.now().plusDays(3))
                        .appointmentStatus(AppointmentStatus.APPROVED)
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .doctor(doctor2)
                        .patient(patient2)
                        .schedule(schedule3)
                        .appointmentDate(LocalDate.now().plusDays(5))
                        .appointmentStatus(AppointmentStatus.PENDING)
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .doctor(doctor3)
                        .patient(patient3)
                        .schedule(schedule4)
                        .appointmentDate(LocalDate.now().plusDays(3))
                        .appointmentStatus(AppointmentStatus.PENDING)
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .doctor(doctor4)
                        .patient(patient4)
                        .schedule(schedule5)
                        .appointmentDate(LocalDate.now().plusDays(6))
                        .appointmentStatus(AppointmentStatus.CANCELLED)
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .doctor(doctor5)
                        .patient(patient5)
                        .schedule(schedule7)
                        .appointmentDate(LocalDate.now().plusDays(5))
                        .appointmentStatus(AppointmentStatus.PENDING)
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .doctor(doctor6)
                        .patient(patient6)
                        .schedule(schedule8)
                        .appointmentDate(LocalDate.now().plusDays(4))
                        .appointmentStatus(AppointmentStatus.CANCELLED)
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .doctor(doctor7)
                        .patient(patient7)
                        .schedule(schedule9)
                        .appointmentDate(LocalDate.now().plusDays(4))
                        .appointmentStatus(AppointmentStatus.PENDING)
                        .build());

        log.debug("Data loaded");
    }
}
