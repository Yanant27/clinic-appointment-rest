package hyk.springframework.clinicappointmentapi.service.security;

import hyk.springframework.clinicappointmentapi.domain.security.Role;
import hyk.springframework.clinicappointmentapi.domain.security.User;
import hyk.springframework.clinicappointmentapi.dto.mapper.UserMapper;
import hyk.springframework.clinicappointmentapi.dto.security.UserAuthenticationDTO;
import hyk.springframework.clinicappointmentapi.dto.security.UserRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.security.UserResponse;
import hyk.springframework.clinicappointmentapi.exception.ResourceAlreadyExistException;
import hyk.springframework.clinicappointmentapi.exception.ResourceNotFoundException;
import hyk.springframework.clinicappointmentapi.repository.security.RoleRepository;
import hyk.springframework.clinicappointmentapi.repository.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Htoo Yanant Khin
 **/
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse authenticateUser(UserAuthenticationDTO userAuthenticationDTO) {
        if (userRepository.findByUsername(userAuthenticationDTO.getUsername()).isPresent()) {
            User user = userRepository.findByUsername(userAuthenticationDTO.getUsername()).get();
            if (passwordEncoder.matches(userAuthenticationDTO.getPassword(), user.getPassword())) {
                return userMapper.userToUserResponse(user);
            } else {
                throw new BadCredentialsException("Incorrect password !");
            }
        } else {
            throw new BadCredentialsException("No user registered with this username !");
        }
    }

    @Override
    public UserResponse saveNewUser(UserRegistrationDTO userRegistrationDTO) {
        if (userRepository.findByUsername(userRegistrationDTO.getUsername()).isPresent()) {
            throw new ResourceAlreadyExistException("Username already exists !");
        }
        // Create new user with input roles
        Set<Role> roles = userRegistrationDTO.getRoles().stream()
                .map(role -> roleRepository.findByNameEqualsIgnoreCase(role)
                        .orElseThrow(() -> new ResourceNotFoundException("Invalid role !")))
                .collect(Collectors.toSet());
        User savedUser = User.builder()
                .username(userRegistrationDTO.getUsername())
                .password(passwordEncoder.encode(userRegistrationDTO.getPassword()))
                .roles(roles).build();
        return userMapper.userToUserResponse(userRepository.save(savedUser));
    }
}
