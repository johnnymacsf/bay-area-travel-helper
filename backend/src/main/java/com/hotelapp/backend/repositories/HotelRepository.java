package com.hotelapp.backend.repositories;

import com.hotelapp.backend.entities.Hotels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotels, String> {
    Page<Hotels> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}