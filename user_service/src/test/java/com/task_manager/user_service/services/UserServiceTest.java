package com.task_manager.user_service.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.task_manager.user_service.dtos.AuthResponseDto;
import com.task_manager.user_service.dtos.LoginRequestDto;
import com.task_manager.user_service.dtos.RegisterRequestDto;
import com.task_manager.user_service.dtos.UserResponseDto;
import com.task_manager.user_service.models.User;
import com.task_manager.user_service.repositories.IUserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    void register_ShouldReturnAuthResponse() {
        RegisterRequestDto request = new RegisterRequestDto("user@example.com", "user", "password");
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setEmail("user@example.com");

        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class), anyLong())).thenReturn("token");

        AuthResponseDto response = userService.register(request);

        assertNotNull(response);
        assertEquals("token", response.token());
    }

    @Test
    void login_ShouldReturnAuthResponse() {
        LoginRequestDto request = new LoginRequestDto("user", "password");
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setPasswordHahs("encoded");
        user.setEmail("user@example.com");

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encoded")).thenReturn(true);
        when(jwtService.generateToken(any(User.class), anyLong())).thenReturn("token");

        AuthResponseDto response = userService.login(request);

        assertNotNull(response);
        assertEquals("token", response.token());
    }

    @Test
    void getUserById_ShouldReturnUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setEmail("user@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDto response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals("user", response.username());
    }
}
