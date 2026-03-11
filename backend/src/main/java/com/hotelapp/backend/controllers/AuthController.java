package com.hotelapp.backend.controllers;

import com.hotelapp.backend.dtos.AuthRequest;
import com.hotelapp.backend.security.PasswordUtil;
import com.hotelapp.backend.security.SessionContents;
import com.hotelapp.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService service;

    public AuthController(UserService service){
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request){
        if (!PasswordUtil.isValid(request.getPassword())) {
            return ResponseEntity.badRequest()
                    .body("Password must be at least 8 characters, include a number and a special character");
        }

        boolean ok = service.register(request.getUsername(), request.getPassword());
        return ok
                ? ResponseEntity.ok("Registered")
                : ResponseEntity.badRequest().body("Username already exists");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletRequest httpRequest){
        return service.login(request.getUsername(), request.getPassword())
                .map(user -> {
                    var session = httpRequest.getSession(true);
                    session.setAttribute(SessionContents.USERNAME, user.getUsername());
                    session.setAttribute(SessionContents.LAST_LOGIN, user.getLastLogin());

                    String message = (user.getLastLogin() != null)
                        ? "Last login: " + user.getLastLogin() : "Welcome! This is your first time logging in!";

                    return ResponseEntity.ok(message);
                }).orElse(ResponseEntity.status(401).body("Invalid credentials"));
    }

    @GetMapping("/session")
    public ResponseEntity<?> sessionInfo(HttpServletRequest request){
        var session = request.getSession(false);
        if(session == null){
            return ResponseEntity.status(401).body("No active session");
        }
        Object username = session.getAttribute(SessionContents.USERNAME);
        if (username == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }

        Object lastLogin = session.getAttribute(SessionContents.LAST_LOGIN);

        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("lastLogin", lastLogin);

        return ResponseEntity.ok(data);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        var session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return ResponseEntity.ok("Logged out");
    }
}
