package hyk.springframework.clinicappointmentapi.service.security;

import hyk.springframework.clinicappointmentapi.domain.security.Role;
import hyk.springframework.clinicappointmentapi.domain.security.User;
import hyk.springframework.clinicappointmentapi.dto.mapper.UserMapper;
import hyk.springframework.clinicappointmentapi.dto.security.RoleResponse;
import hyk.springframework.clinicappointmentapi.dto.security.UserAuthenticationDTO;
import hyk.springframework.clinicappointmentapi.dto.security.UserRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.security.UserResponse;
import hyk.springframework.clinicappointmentapi.exception.ResourceAlreadyExistException;
import hyk.springframework.clinicappointmentapi.exception.ResourceNotFoundException;
import hyk.springframework.clinicappointmentapi.repository.security.RoleRepository;
import hyk.springframework.clinicappointmentapi.repository.security.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Htoo Yanant Khin
 **/
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @DisplayName("Authenticate User")
    @Nested
    class AuthenticateUser {
        @Test
        void authenticateUser_Success() {
            User user = User.builder()
                    .id(1L)
                    .username("admin")
                    .password("admin")
                    .role(Role.builder().name("ADMIN").build())
                    .build();
            UserAuthenticationDTO userAuthenticationDTO = UserAuthenticationDTO.builder()
                    .username("admin")
                    .password("admin")
                    .build();
            UserResponse userResponse = UserResponse.builder()
                    .id(1L)
                    .username("admin")
                    .roles(Set.of(RoleResponse.builder().id(1L).name("ADMIN").build()))
                    .accountNonLocked(true)
                    .enabled(true)
                    .build();

            // Mock method call
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
            when(userMapper.userToUserResponse(any())).thenReturn(userResponse);

            UserResponse returnUser = userService.authenticateUser(userAuthenticationDTO);

            // Verify
            assertEquals(1L, returnUser.getId());
            assertEquals("admin", returnUser.getUsername());
            assertEquals(true, returnUser.getAccountNonLocked());
            assertEquals(true, returnUser.getEnabled());
            assertNotNull(returnUser.getRoles());
            verify(userMapper, times(1)).userToUserResponse(any());
            verify(userRepository, times(2)).findByUsername(anyString());
            verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        }

        @Test
        void authenticateUser_Fail_Invalid_Username() {
            // Test data
            UserAuthenticationDTO userAuthenticationDTO = UserAuthenticationDTO.builder()
                    .username("admin")
                    .password("admin")
                    .build();

            // Mock method call
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

            // Mock exception
            Exception exception = assertThrows(BadCredentialsException.class, () ->
                    userService.authenticateUser(userAuthenticationDTO));

            // Verify
            assertEquals("No user registered with this username !", exception.getMessage());
            verify(userRepository, times(1)).findByUsername(anyString());
        }

        @Test
        void authenticateUser_Fail_Incorrect_Password() {
            // Test data
            User user = User.builder()
                    .id(1L)
                    .username("admin")
                    .password("admin")
                    .role(Role.builder().name("ADMIN").build())
                    .build();
            UserAuthenticationDTO userAuthenticationDTO = UserAuthenticationDTO.builder()
                    .username("admin")
                    .password("admin")
                    .build();

            // Mock method call
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

            // Mock exception
            Exception exception = assertThrows(BadCredentialsException.class, () ->
                    userService.authenticateUser(userAuthenticationDTO));

            // Verify
            assertEquals("Incorrect password !", exception.getMessage());
            verify(passwordEncoder, times(1)).matches(anyString(), anyString());
            verify(userRepository, times(2)).findByUsername(anyString());
        }
    }

    @DisplayName("Register Admin")
    @Nested
    class RegisterAdmin {
        @Test
        void saveNewUser_Success() {
            // Test data
            User user = User.builder()
                    .id(1L)
                    .username("admin")
                    .password("admin")
                    .role(Role.builder().name("ADMIN").build())
                    .build();
            UserRegistrationDTO userRegistrationDTO = UserRegistrationDTO.builder()
                    .username("admin")
                    .password("admin")
                    .roles(Set.of("ADMIN"))
                    .build();
            UserResponse userResponse = UserResponse.builder()
                    .id(1L)
                    .username("admin")
                    .roles(Set.of(RoleResponse.builder().id(1L).name("ADMIN").build()))
                    .accountNonLocked(true)
                    .enabled(true)
                    .build();
            Role role = Role.builder().id(1L).name("ADMIN").build();

            // Mock method call
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
            when(roleRepository.findByNameEqualsIgnoreCase(anyString())).thenReturn(Optional.of(role));
            when(passwordEncoder.encode(anyString())).thenReturn("sfdsfsdfsevewr325wr38rkfnaoerhb");
            when(userMapper.userToUserResponse(any())).thenReturn(userResponse);
            when(userRepository.save(any())).thenReturn(user);

            UserResponse returnUser = userService.saveNewUser(userRegistrationDTO);

            // Verify
            assertEquals(1L, returnUser.getId());
            assertEquals("admin", returnUser.getUsername());
            assertEquals(true, returnUser.getAccountNonLocked());
            assertEquals(true, returnUser.getEnabled());
            assertNotNull(returnUser.getRoles());
            verify(userRepository, times(1)).findByUsername(anyString());
            verify(roleRepository, times(1)).findByNameEqualsIgnoreCase(anyString());
            verify(passwordEncoder, times(1)).encode(anyString());
            verify(userMapper, times(1)).userToUserResponse(any());
        }

        @Test
        void saveNewUser_Fail_Username_Existed() {
            // Test data
            User user = User.builder()
                    .id(1L)
                    .username("admin")
                    .password("admin")
                    .role(Role.builder().name("ADMIN").build())
                    .build();
            UserRegistrationDTO userRegistrationDTO = UserRegistrationDTO.builder()
                    .username("admin")
                    .password("admin")
                    .build();

            // Mock method call
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

            // Mock exception
            Exception exception = assertThrows(ResourceAlreadyExistException.class, () ->
                    userService.saveNewUser(userRegistrationDTO));

            // Verify
            assertEquals("Username already exists !", exception.getMessage());
            verify(userRepository, times(1)).findByUsername(anyString());
        }

        @Test
        void saveNewUser_Fail_Role_NotFound() {
            // Test data
            UserRegistrationDTO userRegistrationDTO = UserRegistrationDTO.builder()
                    .username("admin")
                    .password("admin")
                    .roles(Set.of("GUEST"))
                    .build();

            // Mock method call
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

            // Mock exception
            Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                    userService.saveNewUser(userRegistrationDTO));

            // Verify
            assertEquals("Invalid role !", exception.getMessage());
            verify(userRepository, times(1)).findByUsername(anyString());
        }
    }
}