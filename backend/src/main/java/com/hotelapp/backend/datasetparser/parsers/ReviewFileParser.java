package com.hotelapp.backend.datasetparser.parsers;

import com.google.gson.*;
import com.hotelapp.backend.datasetparser.entities.Review;
import com.hotelapp.backend.datasetparser.repos.ReviewRepository;
import com.hotelapp.backend.datasetparser.repos.ThreadSafeReviewRepository;
import com.hotelapp.backend.mappers.ReviewMapper;
import com.hotelapp.backend.service.DataImportService;


import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;

/**
 * ReviewFileParser class handles parsing the reviews files and storing the reviews in the review repository
 */
public class ReviewFileParser {
    private final DataImportService importService;

    /**
     * ReviewFileParser constructor sets review repository instance
     *
     * @param
     */
    public ReviewFileParser(DataImportService importService) {

        this.importService = importService;
    }

    /**
     * processFiles method takes the directory parameter, parses the reviews json files and creates Review objects with
     * the reviews json data. Since there can be subdirectories in the reviews directory it processes directory
     * recursively, stopping if the directory parameter is a json file and parses the json file. If it isn't a json file,
     * it calls the processFiles method recursively again. Creates a runnable thread to parse each json file
     *
     * @param directory
     */
    public void processFiles(String directory, ExecutorService pool, Phaser phaser) {
        Path p = Paths.get(directory);
        if (Files.isDirectory(p)) {
            try (DirectoryStream<Path> pathsInDir = Files.newDirectoryStream(p)) {
                for (Path path : pathsInDir) {
                    processFiles(path.toString(), pool, phaser);
                }
            } catch (IOException e) {
                System.out.println("Can not open directory: " + directory + " - " + e.getMessage());
            }
        } else if (p.toString().endsWith("json")) {
            phaser.register();
            pool.submit(new Worker(p, phaser));
        }
    }

    /**
     * Review File parser worker parses a review json file and creates a review object from that information
     */
    private class Worker implements Runnable {
        private final Path file;
        private final Phaser phaser;

        /**
         * worker thread constructor takes the review file and phaser, registering the thread with the phaser
         *
         * @param file
         * @param phaser
         */
        Worker(Path file, Phaser phaser) {
            this.file = file;
            this.phaser = phaser;
        }

        /**
         * run method parses the json file grabbing the fields using Gson, if no username is provided set it to anonymous
         * add the created review object to the review repository
         */
        @Override
        public void run() {
            try {
                Gson gson = new Gson();
                try (FileReader fr = new FileReader(file.toString())) {
                    JsonObject jb = (JsonObject) JsonParser.parseReader(fr);
                    JsonObject reviewDetails = jb.getAsJsonObject("reviewDetails");
                    JsonObject reviewCollection = reviewDetails.getAsJsonObject("reviewCollection");
                    JsonArray jsonArray = reviewCollection.getAsJsonArray("review");

                    for (JsonElement element : jsonArray) {
                        JsonObject reviewJson = element.getAsJsonObject();
                        com.hotelapp.backend.datasetparser.entities.Review oldReview =
                                gson.fromJson(reviewJson, com.hotelapp.backend.datasetparser.entities.Review.class);

                        if (oldReview.getUserNickname().isBlank()) {
                            oldReview.setUserNickname("Anonymous");
                        }

                        importService.saveReview(ReviewMapper.map(oldReview));
                    }
                }
            } catch (IOException e) {
                System.out.println("Cannot open JSON file: " + file + " - " + e.getMessage());
            } finally {
                phaser.arriveAndDeregister();
            }
        }
    }
}
