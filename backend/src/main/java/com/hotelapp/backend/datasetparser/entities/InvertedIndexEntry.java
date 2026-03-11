package com.hotelapp.backend.datasetparser.entities;

/**
 * Inverted Index entry class is the value contained in the tree set of the inverted index hashmap
 * has two fields: review and frequency of the key string word in the review text
 * uses comparable to order by frequency in descending order
 */
public class InvertedIndexEntry implements Comparable<InvertedIndexEntry>{
    private Review review;
    private int frequency;

    /**
     * constructor method to set the review and frequency fields
     * @param review the review we want to add
     * @param frequency the frequency the review contains word
     */
    public InvertedIndexEntry(Review review, int frequency){
        this.review = review;
        this.frequency = frequency;
    }

    /**
     * getter method for the frequency field
     * @return frequency
     */
    public int getFrequency(){
        return frequency;
    }

    /**
     * getter method for review
     * @return review object
     */
    public Review getReview(){
        return review;
    }

    /**
     * compareTo method for inverted index entries to be sorted first in descending order in frequency
     * tiebreaker is review date and third tiebreaker is review id
     *
     * @param entry the object to be compared.
     * @return the ordering for the two entries
     */
    @Override
    public int compareTo(InvertedIndexEntry entry){
        int frequencyCompare = Integer.compare(entry.frequency, this.frequency);
        if(frequencyCompare != 0){
            return frequencyCompare;
        }
        String entryDate = entry.getReview().getDate();
        String thisDate = this.getReview().getDate();
        int dateCompare = entryDate.compareTo(thisDate);
        if(dateCompare != 0){
            return dateCompare;
        }

        return entry.getReview().getId().compareTo(this.getReview().getId());
    }
}
