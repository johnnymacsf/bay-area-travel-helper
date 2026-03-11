package com.hotelapp.backend.datasetparser.entities;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread safe inverted index class, extends inverted index, overrides unsafe methods to use
 * reentrant lock, so only one thread can access the protected try block at a time.
 */
public class ThreadSafeInvertedIndex extends InvertedIndex{
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * overrides the unsafe addReview method in the unsafe invertedindex class, uses reentrant lock to lock the write lock
     * so only the current thread has the write lock and can access the try block and add a review to the inverted index map
     * finally block releases the lock after the thread is finished
     * @param review is the review to be added
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
     * overrides the unsafe getReviews method to use reentrant lock, lock the read lock so only the current thread has the
     * read lock and can access the protected data of the inverted index. In the finally block release the read lock
     * @param word the word we want to find in review texts
     * @return
     */
    @Override
    public List<InvertedIndexEntry> getReviews(String word){
        lock.readLock().lock();
        try{
            return super.getReviews(word);
        }finally{
            lock.readLock().unlock();
        }
    }
}
