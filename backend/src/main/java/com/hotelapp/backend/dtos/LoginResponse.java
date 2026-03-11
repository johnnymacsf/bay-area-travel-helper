package com.hotelapp.backend.dtos;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public class LoginResponse {
    private String username;
    private LocalDateTime lastLogin;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
