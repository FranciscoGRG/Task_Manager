package com.task_manager.task_service.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.task_manager.task_service.dtos.UserDto;

@Service
public class UserClient {

    @Autowired
    private RestTemplate restTemplate;

    public UserDto getUserById(Long userId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<UserDto> response = restTemplate.exchange(
                "http://localhost:8081/auth/" + userId,
                HttpMethod.GET,
                entity,
                UserDto.class);

        return response.getBody();
    }
}
