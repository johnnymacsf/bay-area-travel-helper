package com.hotelapp.backend.datasetparser;


import com.hotelapp.backend.datasetparser.entities.Review;

import java.util.Comparator;

/**
 * ReviewComparator handles comparing reviews with each other
 */
public class ReviewComparator implements Comparator<Review> {

    /**
     * ReviewComparator compares two reviews with each other. First it compares the date between the two reviews and if
     * they are equal it then compares their review ids in ascending order. The review with the earlier date is sorted first.
     *
     * @param review1 the first object to be compared.
     * @param review2 the second object to be compared.
     * @return the comparison between review1 and review2
     */
    @Override
    public int compare(Review review1, Review review2){
        int compareByDate = review2.getDate().compareTo(review1.getDate());
        if(compareByDate == 0){
            return review1.getId().compareTo(review2.getId());
        }else{
            return compareByDate;
        }
    }
}
