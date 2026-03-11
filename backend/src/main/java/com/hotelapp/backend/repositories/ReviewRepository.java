package com.hotelapp.backend.repositories;

import com.hotelapp.backend.entities.Reviews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Reviews, String> {
    Page<Reviews> findByHotelIdOrderByDateDesc(String hotelId, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Reviews r WHERE r.hotelId = :hotelId")
    Double findAverageRatingByHotelId(@Param("hotelId") String hotelId);
}
