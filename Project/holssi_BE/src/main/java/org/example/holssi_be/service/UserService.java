package org.example.holssi_be.service;

import org.example.holssi_be.entity.domain.Users;
import org.example.holssi_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Users save(Users user) {
        return userRepository.save(user);
    }
}