package com.hotelapp.backend.service;

import com.hotelapp.backend.entities.Hotels;
import com.hotelapp.backend.repositories.HotelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository){
        this.hotelRepository = hotelRepository;
    }

    public Page<Hotels> searchHotels(String keyword, int page, int pageSize){
        int safePage = Math.max(page, 1);
        var pageable = PageRequest.of(
                safePage - 1,
                pageSize,
                Sort.by("name").ascending()
        );
        if(keyword == null || keyword.trim().isEmpty()){
            return hotelRepository.findAll(pageable);
        }

        return hotelRepository.findByNameContainingIgnoreCase(keyword.trim(), pageable);
    }

    public Optional<Hotels> getHotelById(String hotelId){
        return hotelRepository.findById(hotelId);
    }
}
