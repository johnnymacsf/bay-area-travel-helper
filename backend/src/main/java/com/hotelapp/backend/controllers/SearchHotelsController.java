package com.hotelapp.backend.controllers;

import com.hotelapp.backend.security.SessionUtil;
import com.hotelapp.backend.service.HotelService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hotels")
public class SearchHotelsController {
    private final HotelService hotelService;

    public SearchHotelsController(HotelService hotelService){
        this.hotelService = hotelService;
    }

    @GetMapping
    public ResponseEntity<?> searchHotels(
        @RequestParam(required = false, defaultValue = "") String keyword,
        @RequestParam(required = false, defaultValue = "1") int page,
        HttpServletRequest request
            ){
        if(!SessionUtil.isUserLoggedIn(request)){
            return ResponseEntity.status(401).body("Please log into your account!");
        }
        int pageSize = 10;
        var resultPage = hotelService.searchHotels(keyword, page, pageSize);

        Map<String, String> hotelNames = resultPage.getContent().stream()
                .collect(Collectors.toMap(
                        h -> h.getHotelId(),
                        h -> h.getName(),
                        (a,b) -> a,
                        LinkedHashMap::new
                ));
        Map<String, Object> response = Map.of(
                "keyword", keyword,
                "page", page,
                "pageSize", pageSize,
                "prevPage", Math.max(page - 1, 1),
                "nextPage", resultPage.hasNext() ? page +1 : page,
                "hasNextPage", resultPage.hasNext(),
                "hotelNames", hotelNames
        );
        return ResponseEntity.ok(response);
    }
}
