package hyk.springframework.clinicappointmentapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ClinicAppointmentApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClinicAppointmentApiApplication.class, args);
    }

}
