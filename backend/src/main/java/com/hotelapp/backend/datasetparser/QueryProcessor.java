package com.hotelapp.backend.datasetparser;



import com.hotelapp.backend.datasetparser.entities.InvertedIndexEntry;
import com.hotelapp.backend.datasetparser.entities.Review;
import com.hotelapp.backend.datasetparser.repos.HotelRepository;
import com.hotelapp.backend.datasetparser.repos.ThreadSafeReviewRepository;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;

/**
 * QueryProcessor class with query thread worker
 */
public class QueryProcessor {
    private final HotelRepository hotelRepo;
    private final ThreadSafeReviewRepository reviewRepo;
    private final Path outputDirectory = Paths.get("output").toAbsolutePath();

    /**
     * constructo for query processor, initializes the review and hotel repositories
     * @param hotelRepo
     * @param reviewRepo
     */
    public QueryProcessor(HotelRepository hotelRepo, ThreadSafeReviewRepository reviewRepo){
        this.hotelRepo = hotelRepo;
        this.reviewRepo = reviewRepo;
    }

    /**
     * process method, takes the query argument path from the command line argument -queries, and the thread pool and
     * phaser. Traverses the file directory recursively until it finds a query file with the .txt end
     * creates a query worker once it finds this file, registers it, and runs it.
     *
     * @param queryPath
     * @param pool
     * @param phaser
     */
    public void process(String queryPath, ExecutorService pool, Phaser phaser) {
        Path p = Paths.get(queryPath);
        if(Files.isDirectory(p)){
            try(DirectoryStream<Path> pathsInDir = Files.newDirectoryStream(p)){
                for(Path path : pathsInDir){
                    process(path.toString(), pool, phaser);
                }
            }catch(Exception e){
                System.out.println(e);
            }
        }else if(p.toString().endsWith("txt")){
            phaser.register();
            pool.submit(new QueryWorker(p, phaser));
        }
    }

    /**
     * QueryWorker, thread worker
     */
    private class QueryWorker implements Runnable{
        private final Path file;
        private final Phaser phaser;

        /**
         * constructor, takes the query file and phaser
         * @param file
         * @param phaser
         */
        QueryWorker(Path file, Phaser phaser) {
            this.file = file;
            this.phaser = phaser;
        }

        /**
         * run method, gets the contents of the query, so the query itself and the parameter,
         * runs the command, gets the output and puts it in a created output file
         */
        @Override
        public void run() {
            try{
                String query = Files.readString(file);
                String[] parts = query.strip().split("\\s+");
                String queryCommand = parts[0];
                String querySecondPart = parts[1];
                StringBuilder output = new StringBuilder();

                if(!Files.exists(outputDirectory)){
                    Files.createDirectories(outputDirectory);
                }

                if(queryCommand.equals("findHotel")){
                    output.append(hotelRepo.getHotel(querySecondPart).queryOutput());
                }else if(queryCommand.equals("findReviews")){
                    List<Review> reviews =  reviewRepo.getReviews(querySecondPart);
                    for(Review review : reviews){
                        output.append(review.outputToFile());
                    }
                }else if(queryCommand.equals("findWord")){
                    List<InvertedIndexEntry> reviewsWithWord = reviewRepo.getReviewsContainingWord(querySecondPart);
                    for(InvertedIndexEntry entry : reviewsWithWord){
                        Review review = entry.getReview();
                        output.append(review.outputToFile());
                    }
                }
                String outputFileName = file.getFileName().toString().replace(".txt", ".out");
                Path outputFile = file.getParent().resolve(outputFileName);
                Files.writeString(outputFile, output.toString());
            }catch(Exception e){
                System.out.println("Cannot open txt file: " + file + " - " + e.getMessage());
            }finally{
                phaser.arriveAndDeregister();
            }
        }
    }
}
