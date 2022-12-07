package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.security.Role;
import hyk.springframework.clinicappointmentapi.dto.security.RoleResponse;
import org.mapstruct.Mapper;

/**
 * @author Htoo Yanant Khin
 **/
@Mapper
public interface RoleMapper {
    RoleResponse roleToRoleResponse(Role role);
}
