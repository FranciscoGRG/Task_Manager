package com.task_manager.user_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.task_manager.user_service.dtos.AuthResponseDto;
import com.task_manager.user_service.dtos.LoginRequestDto;
import com.task_manager.user_service.dtos.RegisterRequestDto;
import com.task_manager.user_service.dtos.UserResponseDto;
import com.task_manager.user_service.models.User;
import com.task_manager.user_service.repositories.IUserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Transactional
    public AuthResponseDto register(RegisterRequestDto dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new RuntimeException("El username ya esta en uso");
        }

        User newUser = new User();
        newUser.setUsername(dto.username());
        newUser.setEmail(dto.email());
        newUser.setPasswordHahs(passwordEncoder.encode(dto.password()));

        User savedUser = userRepository.save(newUser);
        String token = jwtService.generateToken(savedUser, savedUser.getId());

        return new AuthResponseDto(token, savedUser.getEmail(), savedUser.getUsername());
    }

    public AuthResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.password(), user.getPasswordHahs())) {
            throw new RuntimeException("Credenciales invalidas");
        }

        String token = jwtService.generateToken(user, user.getId());
        return new AuthResponseDto(token, user.getEmail(), user.getUsername());
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
