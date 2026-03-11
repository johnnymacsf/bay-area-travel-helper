package com.hotelapp.backend.service;


import com.hotelapp.backend.datasetparser.entities.Hotel;
import com.hotelapp.backend.entities.Hotels;
import com.hotelapp.backend.entities.Reviews;
import com.hotelapp.backend.repositories.HotelRepository;
import com.hotelapp.backend.repositories.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class DataImportService {
    private final HotelRepository hotelRepository;
    private final ReviewRepository reviewRepository;

    public DataImportService(HotelRepository hotelRepository, ReviewRepository reviewRepository){
        this.hotelRepository = hotelRepository;
        this.reviewRepository = reviewRepository;
    }

    public void saveHotels(List<Hotels> hotels){
        for(Hotels hotel : hotels){
            if(!hotelRepository.existsById(hotel.getHotelId())){
                hotelRepository.save(hotel);
            }
        }
    }

    public void saveHotel(Hotels hotel){
        hotelRepository.save(hotel);
    }

    public void saveReviews(List<Reviews> reviews){
        for(Reviews review : reviews){
            if(!reviewRepository.existsById(review.getReviewId())){
                reviewRepository.save(review);
            }
        }
    }

    public void saveReview(Reviews review){
        reviewRepository.save(review);
    }

}
