package hyk.springframework.clinicappointmentapi.repository.security;

import hyk.springframework.clinicappointmentapi.domain.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Htoo Yanant Khin
 **/
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNameEqualsIgnoreCase(String name);
}
