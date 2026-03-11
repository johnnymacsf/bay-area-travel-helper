package com.hotelapp.backend.service;

import com.hotelapp.backend.entities.Reviews;
import com.hotelapp.backend.repositories.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    public Page<Reviews> getReviewsForHotel(String hotelId, int page, int pageSize){
        int safePage = Math.max(page,1);
        var pageable = PageRequest.of(safePage - 1, pageSize);
        return reviewRepository.findByHotelIdOrderByDateDesc(hotelId, pageable);
    }

    public double getAverageRatingForHotel(String hotelId){
        Double avg = reviewRepository.findAverageRatingByHotelId(hotelId);
        return avg == null ? 0.0 : avg;
    }

    public void addReview(Reviews review){
        reviewRepository.save(review);
    }

}
