package com.hotelapp.backend.datasetparser.repos;


import com.hotelapp.backend.datasetparser.entities.Review;
import com.hotelapp.backend.datasetparser.entities.InvertedIndexEntry;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ThreadSafeReveiwRepository extends the unsafe review repository using the reetrant read write lock to protect the
 * inverted index map and ensure only one thread access/manipulates it at a time.
 */
public class ThreadSafeReviewRepository extends ReviewRepository{
    private ReentrantReadWriteLock lock;

    /**
     * constructor, initializes the instance of the reentrant lock
     */
    public ThreadSafeReviewRepository(){
        lock = new ReentrantReadWriteLock();
    }

    /**
     * override the unsafe addReview method from the review repository, making it safe by using the reentrant lock
     * to lock the write lock so only the current thread has it and can add a review to the review repository hashmap
     * It calls the "original" (parent class's) addReview method, however it is now safe as only the current thread accesses it
     * @param review to be added
     */
    @Override
    public void addReview(Review review){
        lock.writeLock().lock();
        try{
            super.addReview(review);
        }finally{
            lock.writeLock().unlock();
        }
    }

    /**
     * override the original, unsafe getReviews method allowing concurrent reading if no write lock has been acquired.
     * @param hotelId the hotelId we want to get reviews for
     * @return
     */
    @Override
    public List<Review> getReviews(String hotelId){
        lock.readLock().lock();
        try{
            return super.getReviews(hotelId);
        }finally{
            lock.readLock().unlock();
        }
    }

    /**
     * getAllReviews overrides the thread unsafe getAllReviews() method from the parent reviews repository class
     * It makes it thread safe by locking the read lock to allow for concurrent reading.
     * @return
     */
    @Override
    public List<Review> getAllReviews(){
        lock.readLock().lock();
        try{
            return super.getAllReviews();
        }finally{
            lock.readLock().unlock();
        }
    }

    /**
     * override the unsafe method and lock the read lock so the current thread has a read lock, allows for concurrent
     * reading and makes sure it is not reading data being manipulated by a write lock.
     * @param word is the word we want to look for in the reviews
     * @return
     */
    @Override
    public List<InvertedIndexEntry> getReviewsContainingWord(String word){
        lock.readLock().lock();
        try{
            return super.getReviewsContainingWord(word);
        }finally{
            lock.readLock().unlock();
        }
    }
}
