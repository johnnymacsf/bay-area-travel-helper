package com.hotelapp.backend.controllers;

import com.hotelapp.backend.security.SessionUtil;
import com.hotelapp.backend.service.HotelService;
import com.hotelapp.backend.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {
    private final HotelService hotelService;
    private final ReviewService reviewService;

    public HotelController(HotelService hotelService, ReviewService reviewService){
        this.hotelService = hotelService;
        this.reviewService = reviewService;
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<?> getHotelInfo(
            @PathVariable String hotelId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            HttpServletRequest request
    ){
        if(!SessionUtil.isUserLoggedIn(request)){
            return ResponseEntity.status(401).body("Please log into your account!");
        }
        var hotelOpt = hotelService.getHotelById(hotelId);
        if(hotelOpt.isEmpty()){
            return ResponseEntity.status(404).body("Hotel not found");
        }
        var hotel = hotelOpt.get();

        var reviewsPage = reviewService.getReviewsForHotel(hotelId,page, pageSize);

        String expediaLink = "https://www.expedia.com/" + hotel.getCity().replaceAll(" ", "-")
                + "-Hotels" + hotel.getName().replaceAll(" ", "-").replaceAll("/", "") + ".h" + hotel.getHotelId()
                + ".Hotel-Information";
        double averageRating = reviewService.getAverageRatingForHotel(hotelId);

        Map<String, Object> response = new HashMap<>();
        response.put("hotel", hotel);
        response.put("expediaLink", expediaLink);
        response.put("averageRating", averageRating);
        response.put("page", page);
        response.put("pageSize", pageSize);
        response.put("hasNextPage", reviewsPage.hasNext());
        response.put("hasPrevPage", reviewsPage.hasPrevious());
        response.put("totalReviewsOnThisPage", reviewsPage.getNumberOfElements());
        response.put("reviews", reviewsPage.getContent());

        return ResponseEntity.ok(response);
    }
}