package com.hotelapp.backend.controllers;

import com.hotelapp.backend.ai.HotelReviewAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class HotelAiController {
    private final HotelReviewAiService aiService;

    public HotelAiController(HotelReviewAiService aiService){
        this.aiService = aiService;
    }

    @GetMapping("/hotel/{hotelId}/pro-cons")
    public ResponseEntity<?> getHotelProsAndCons(@PathVariable String hotelId){
        String prosCons = aiService.generateProsAndCons(hotelId);
        Map<String, String> response = new HashMap<>();
        response.put("prosCons", prosCons);
        return ResponseEntity.ok(response);
    }
}
