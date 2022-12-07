package hyk.springframework.clinicappointmentapi.domain.security;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Htoo Yanant Khin
 **/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
