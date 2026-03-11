package com.hotelapp.backend;

import com.hotelapp.backend.datasetparser.parsers.HotelFileParser;
import com.hotelapp.backend.datasetparser.parsers.ReviewFileParser;
import com.hotelapp.backend.service.DataImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

@Component
public class DataLoader implements CommandLineRunner {
    private final DataImportService importService;

    public DataLoader(DataImportService importService){
        this.importService = importService;
    }
    @Override
    public void run(String... args) throws Exception {
        String hotelPath ="dataset/hotels/hotels.json";
        String reviewPath = "dataset/reviews";

        HotelFileParser hotelFileParser = new HotelFileParser(importService);
        ReviewFileParser reviewFileParser = new ReviewFileParser(importService);

        ExecutorService pool = Executors.newFixedThreadPool(8);
        Phaser phaser = new Phaser(1);

        hotelFileParser.processFiles(hotelPath);

        reviewFileParser.processFiles(reviewPath,pool,phaser);
        phaser.arriveAndAwaitAdvance();
        pool.shutdown();
    }
}
