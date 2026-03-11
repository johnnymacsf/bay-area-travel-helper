package com.hotelapp.backend.controllers;

import com.hotelapp.backend.security.SessionContents;
import com.hotelapp.backend.security.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    @GetMapping
    public ResponseEntity<?> home(HttpServletRequest request){
        if(!SessionUtil.isUserLoggedIn(request)){
            return ResponseEntity.status(401).body("Please log into your account!");
        }

        String username = (String) request.getSession().getAttribute(SessionContents.USERNAME);
        Object lastLogin = request.getSession().getAttribute(SessionContents.LAST_LOGIN);

        return ResponseEntity.ok(
                Map.of("username", username,
                        "lastLogin", lastLogin)
        );
    }
}
