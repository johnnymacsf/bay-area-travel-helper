package com.hotelapp.backend.datasetparser.entities;

import java.util.*;

/**
 * Inverted Index class maps words to reviews which contain the word in the review text
 * Uses a tree set as value so that reviews are ordered in descending order of frequency
 */
public class InvertedIndex{
    private Map<String, TreeSet<InvertedIndexEntry>> invertedIndex = new HashMap<>();

    /**
     * adds a review to the invertedIndex map, splitting the words in review text by white space and punctuation before
     * getting the frequency and putting it in the inverted index, sorted in descending order
     * @param review is the review to be added
     */
    public void addReview(Review review){
        String reviewText = review.getReviewText().toLowerCase();
        String[] words = reviewText.split("[\\s,;:.!?]+");
        Map<String, Integer> wordCount = new HashMap<>();
        for(String word : words){
            wordCount.put(word, wordCount.getOrDefault(word,0)+1);
        }
        for(Map.Entry<String, Integer> entry : wordCount.entrySet()){
            String word = entry.getKey();
            int frequency = entry.getValue();
            invertedIndex.computeIfAbsent(word, k -> new TreeSet<>()).add(new InvertedIndexEntry(review, frequency));
        }
    }

    /**
     * getReviews method returns reviews containing word, using the inverted index to retrieve said reviews
     * grabs the tree set value from the inverted index hashmap from the key of word
     * @param word the word we want to find in review texts
     * @return a list of reviews
     */
    public List<InvertedIndexEntry> getReviews(String word){
        TreeSet<InvertedIndexEntry> reviews = invertedIndex.get(word.toLowerCase());
        return new ArrayList<>(reviews);
    }
}
