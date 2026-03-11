package com.hotelapp.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "reviews")
public class Reviews {

    @Id
    @Column(name = "reviewId", length = 255)
    private String reviewId;

    @Column(name = "hotelId", length = 255)
    private String hotelId;

    private Double rating;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(length = 255)
    private String userNickname;

    private LocalDate date;

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Reviews(String reviewId, String hotelId, Double rating, String title, String text, String userNickname, LocalDate date) {
        this.reviewId = reviewId;
        this.hotelId = hotelId;
        this.rating = rating;
        this.title = title;
        this.text = text;
        this.userNickname = userNickname;
        this.date = date;
    }

    public Reviews(){

    }
}
