package com.task_manager.user_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task_manager.user_service.dtos.AuthResponseDto;
import com.task_manager.user_service.dtos.LoginRequestDto;
import com.task_manager.user_service.dtos.RegisterRequestDto;
import com.task_manager.user_service.dtos.UserResponseDto;
import com.task_manager.user_service.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(service.login(request));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@RequestParam Long id) {
        return ResponseEntity.ok(service.getUserById(id));
    }
    

}
