package com.hotelapp.backend.service;

import com.hotelapp.backend.dtos.LoginResponse;
import com.hotelapp.backend.entities.Users;
import com.hotelapp.backend.repositories.UserRepository;
import com.hotelapp.backend.security.PasswordUtil;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public boolean register(String username, String password){
        if(userRepository.existsByUsername(username)){
            return false;
        }

        if(!PasswordUtil.isValid(password)){
            return false;
        }

        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hashPassword(password, salt);

        Users user = new Users();
        user.setUsername(username);
        user.setPassword(hash);
        user.setUsersalt(salt);

        userRepository.save(user);
        return true;
    }

    public Optional<LoginResponse> login(String username, String password) {
        return userRepository.findByUsername(username).flatMap(user -> {
            String hash = PasswordUtil.hashPassword(password, user.getUsersalt());

            if (!hash.equals(user.getPassword())) {
                return Optional.empty();
            }

            // Capture previous login
            LocalDateTime previousLogin = user.getLastLogin();

            // Update DB with current login
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            // Build response DTO
            LoginResponse response = new LoginResponse();
            response.setUsername(user.getUsername());
            response.setLastLogin(previousLogin);

            return Optional.of(response);
        });
    }

}
