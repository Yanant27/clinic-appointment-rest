# Secured Clinic Appointment REST API with JWT Authentication
Spring Boot REST API for creating schedule for doctors, creating appointments to see doctors, adding and updating information of doctors and patients.

In this API, 
- use **lombok** to remove biolar plate code for POJO class.
- use **DTO Pattern (Data Transfer Object)**.
- use **mapstruct** for easy map between different object models (e.g. entities and DTOs).
- create **custom validator** for specified phone number format.
- write **unit test and integration test** for REST endpoints.

## Requirements
- JDK 1.8 or higher
- Spring Boot 2.7.4
- Maven 3

## Dependencies
- Spring Web
- Spring Boot DevTools
- Spring Data JPA
- Spring Security
- Json Web Token
- Lombok
- MapStruct
- H2 Database
- MySQL Driver
- Validation
- Spring Security Test
- Spring Boot Test

**Browse the Maven pom.xml file for details of libraries and versions used.**

## Building the project
This application use embedded Tomcat 8. Therefore, no Tomcat or JBoss installation is necessary.
1. Run in IDE
    - Clone github repository with command `git clone https://github.com/Yanant27/clinic-appointment-rest.git` and import project into IDE.
    - Or download `.zip` file, unzip and import project into IDE.
    - Execute the `main` method in the `hyk.springframework.clinicappointmentapi.ClinicAppointmentApiApplication` class from your IDE.
2. Run with maven
    - You can build the project and run the tests by running `mvn clean package`.
    - Once successfully built, you can run by one of these two methods.
    ```    
        java -jar -Dspring.profiles.active=dev target/clinic-appointment-api-0.0.1-SNAPSHOT.jar
        or
        mvn spring-boot:run -Drun.arguments="spring.profiles.active=test"
    ```
