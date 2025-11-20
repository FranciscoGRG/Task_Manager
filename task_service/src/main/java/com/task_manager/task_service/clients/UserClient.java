package com.task_manager.task_service.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.task_manager.task_service.dtos.UserDto;

@Service
public class UserClient {

    @Autowired
    private RestTemplate restTemplate;

    public UserDto getUserById(Long userId) {
        return restTemplate.getForObject(
                "http://localhost:8082/users/" + userId,
                UserDto.class);
    }
}
