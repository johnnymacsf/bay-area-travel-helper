package com.hotelapp.backend.mappers;

import com.hotelapp.backend.datasetparser.entities.Review;
import com.hotelapp.backend.entities.Reviews;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public class ReviewMapper {
    public static Reviews map(Review oldReview){
        Reviews entity = new Reviews();
        entity.setReviewId(oldReview.getId());
        entity.setHotelId(oldReview.getHotelId());
        entity.setDate(LocalDate.parse(oldReview.getDate()));
        entity.setText(oldReview.getReviewText());
        entity.setRating(oldReview.getRating());
        entity.setTitle(oldReview.getReviewTitle());
        entity.setUserNickname(oldReview.getUserNickname());

        return entity;
    }
}
