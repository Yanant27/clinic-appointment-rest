package hyk.springframework.clinicappointmentapi.repository.security;

import hyk.springframework.clinicappointmentapi.domain.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Htoo Yanant Khin
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
