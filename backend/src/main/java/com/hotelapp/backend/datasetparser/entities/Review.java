package com.hotelapp.backend.datasetparser.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Review class used to create Review objects from review json file, serialized name fields to map review object fields to
 * their json counterparts
 */
public class Review {
    @SerializedName("hotelId")
    private String hotelId;
    @SerializedName("reviewId")
    private String reviewId;
    @SerializedName("ratingOverall")
    private double overallRating;
    @SerializedName("title")
    private String reviewTitle;
    @SerializedName("reviewText")
    private String reviewText;
    @SerializedName("userNickname")
    private String userNickname;
    @SerializedName("reviewSubmissionDate")
    private String dateOfReview;

    /**
     * Review class constructor
     * @param hotelId
     * @param reviewId
     * @param overallRating
     * @param reviewTitle
     * @param reviewText
     * @param userNickname
     * @param dateOfReview
     */
    public Review(String hotelId, String reviewId, Double overallRating, String reviewTitle, String reviewText, String userNickname, String dateOfReview){
        this.hotelId = hotelId;
        this.reviewId = reviewId;
        this.overallRating = overallRating;
        this.reviewTitle = reviewTitle;
        this.reviewText = reviewText;
        this.userNickname = userNickname;
        this.dateOfReview = dateOfReview;
    }

    /**
     * getter for review id
     * @return review id
     */
    public String getId(){
        return reviewId;
    }

    public String getUserNickname(){
        return userNickname;
    }

    public void setUserNickname(String nickname){
        this.userNickname = nickname;
    }

    /**
     * getter for hotel id
     * @return hotelId for the review
     */
    public String getHotelId(){
        return hotelId;
    }

    /**
     * getter for review date
     * @return the date of the review
     */
    public String getDate(){
        return dateOfReview;
    }

    /**
     * getter for review text
     * @return reviewText for the review
     */
    public String getReviewText(){
        return reviewText;
    }

    public String getReviewTitle(){ return reviewTitle; }

    public double getRating(){ return overallRating; }



    /**
     * toString() method returns review information formatted in a string
     * @return review information string
     */
    @Override
    public String toString() {
        return "hotelId = " + hotelId + System.lineSeparator()
                + "reviewId = " + reviewId + System.lineSeparator()
                + "averageRating = " + overallRating + System.lineSeparator()
                + "title = " + reviewTitle + System.lineSeparator()
                + "reviewText = " + reviewText + System.lineSeparator()
                + "userNickname = " + userNickname + System.lineSeparator()
                + "submissionDate = " + dateOfReview + System.lineSeparator();
    }

    /**
     * output to file, organizes the output to the output file
     * @return String output being written to the output file
     */
    public String outputToFile(){
        StringBuilder sb = new StringBuilder();
        String authorName = "";
        if(userNickname.equals("")){
            authorName = "Anonymous";
        }else{
            authorName = userNickname;
        }
        sb.append("Review by ").append(authorName).append(" on ").append(dateOfReview).append(System.lineSeparator());
        sb.append("Rating: ").append((int)overallRating).append(System.lineSeparator());
        sb.append("ReviewId: ").append(reviewId).append(System.lineSeparator());
        sb.append(reviewTitle).append(System.lineSeparator());
        sb.append(reviewText);
        return sb.toString();
    }
}
