package com.hotelapp.backend.datasetparser.repos;


import com.hotelapp.backend.datasetparser.ReviewComparator;
import com.hotelapp.backend.datasetparser.entities.InvertedIndexEntry;
import com.hotelapp.backend.datasetparser.entities.Review;
import com.hotelapp.backend.datasetparser.entities.ThreadSafeInvertedIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * Review Repository class helps manage reviews and stores a list of all reviews in a hashmap, with the primary key being
 * hotelId and the value being a treeset of those reviews for a specific hotel, ordered by the review comparator.
 * Also stores the inverted index, used to map each word to the list of reviews in which the review text contains the word in it.
 */
public class ReviewRepository {
    private final HashMap<String, TreeSet<Review>> reviews = new HashMap<>();
    private final ThreadSafeInvertedIndex invertedIndex = new ThreadSafeInvertedIndex();

    /**
     * addReview method first checks if the reviews map contains the hotelId key, if not it puts it in the map and creates
     * an empty tree set as the value, using review comparator as the ordering. It adds the review as the value for the
     * key with the review's hotelId and calls the inverted index's add review method to update the inverted index using
     * that review's review text.
     * @param review to be added
     */
    public void addReview(Review review){
        if(!reviews.containsKey(review.getHotelId())){
            reviews.put(review.getHotelId(), new TreeSet<>(new ReviewComparator()));
        }
        reviews.get(review.getHotelId()).add(review);
        invertedIndex.addReview(review);
    }

    /**
     * getReviews method returns a list of reviews for a hotel
     * @param hotelId the hotelId we want to get reviews for
     * @return an array list of reviews for a given hotelId, converted from a TreeSet of reviews
     */
    public List<Review> getReviews(String hotelId){
        if(!reviews.containsKey(hotelId)){
            return new ArrayList<>();
        }
        return new ArrayList<>(reviews.get(hotelId));
    }

    /**
     * getAllReviews method returns all the reviews in the reviews hashmap
     * @return a list of all the reviews from the hashmap reviews
     */
    public List<Review> getAllReviews(){
        List<Review> allReviews = new ArrayList<>();
        for(TreeSet<Review> hotelReviews : reviews.values()){
            allReviews.addAll(hotelReviews);
        }
        return allReviews;
    }

    /**
     * getReviewsContainingWord method returns all reviews in which the review text contains at least one occurence
     * of word in it, using the created inverted index.
     * @param word is the word we want to look for in the reviews
     * @return a list of reviews containing word, called from the inverted index
     */
    public List<InvertedIndexEntry> getReviewsContainingWord(String word){
        return invertedIndex.getReviews(word);
    }
}
