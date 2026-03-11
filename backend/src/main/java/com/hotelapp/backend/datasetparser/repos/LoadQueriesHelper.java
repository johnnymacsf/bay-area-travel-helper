package com.hotelapp.backend.datasetparser.repos;



import com.hotelapp.backend.datasetparser.QueryProcessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class LoadQueriesHelper {
    private final ExecutorService pool;
    private final Phaser phaser;
    private HotelRepository hotelRepo;
    private ThreadSafeReviewRepository reviewRepo;

    public LoadQueriesHelper(HotelRepository hotelRepo, ThreadSafeReviewRepository reviewRepo, int numThreads, Phaser phaser){
        this.phaser = phaser;
        this.hotelRepo = hotelRepo;
        this.reviewRepo = reviewRepo;
        this.pool = Executors.newFixedThreadPool(numThreads);
    }

    public void processQueries(String queryPath){
        if(queryPath == null) return;

        QueryProcessor qp = new QueryProcessor(hotelRepo, reviewRepo);
        qp.process(queryPath, pool, phaser);

        phaser.arriveAndAwaitAdvance();
        pool.shutdown();
        try{
            pool.awaitTermination(1, TimeUnit.MINUTES);
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
