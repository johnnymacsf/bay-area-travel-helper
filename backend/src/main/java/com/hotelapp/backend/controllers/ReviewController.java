package com.hotelapp.backend.controllers;

import com.hotelapp.backend.entities.Reviews;
import com.hotelapp.backend.security.SessionContents;
import com.hotelapp.backend.security.SessionUtil;
import com.hotelapp.backend.service.HotelService;
import com.hotelapp.backend.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final HotelService hotelService;

    public ReviewController(ReviewService reviewService, HotelService hotelService){
        this.reviewService = reviewService;
        this.hotelService = hotelService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReview(
            @RequestBody Reviews review,
            HttpServletRequest request
    ){
        if(!SessionUtil.isUserLoggedIn(request)){
            return ResponseEntity.status(401).body("Please log into your account!");
        }

        var hotelOpt = hotelService.getHotelById(review.getHotelId());
        if(hotelOpt.isEmpty()){
            return ResponseEntity.status(404).body("Hotel not found.");
        }

        String username = (String) request.getSession().getAttribute(SessionContents.USERNAME);

        review.setReviewId(UUID.randomUUID().toString());
        review.setUserNickname(username);
        review.setDate(LocalDate.now());

        reviewService.addReview(review);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Review added successfully!");
        response.put("hotelId", review.getHotelId());

        return ResponseEntity.ok(response);
    }
}
