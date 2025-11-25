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

    @org.springframework.beans.factory.annotation.Value("${user.service.url}")
    private String userServiceUrl;

    public UserDto getUserById(Long userId, String token) {
        // The endpoint /auth/{id} is public, so we don't strictly need the token.
        // Removing it to avoid potential header issues (400 Bad Request).
        HttpHeaders headers = new HttpHeaders();
        // headers.set("Authorization", token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<UserDto> response = restTemplate.exchange(
                userServiceUrl + "/auth/" + userId,
                HttpMethod.GET,
                entity,
                UserDto.class);

        return response.getBody();
    }
}
