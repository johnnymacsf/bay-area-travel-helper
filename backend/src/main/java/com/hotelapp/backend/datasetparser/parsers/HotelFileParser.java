package com.hotelapp.backend.datasetparser.parsers;

import com.google.gson.*;
import com.hotelapp.backend.datasetparser.entities.Hotel;
import com.hotelapp.backend.datasetparser.repos.HotelRepository;
import com.hotelapp.backend.entities.Hotels;
import com.hotelapp.backend.mappers.HotelMapper;
import com.hotelapp.backend.service.DataImportService;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HotelFileParser {
    private final DataImportService importService;

    /**
     * HotelFileParser constructor
     *
     */

    public HotelFileParser(DataImportService importService){
        this.importService = importService;
    }

    /**
     * processFiles method takes the file path to the hotel json file and parses it, using the information in it to
     * construct Hotel objects and adds them to the hotel repository
     * @param file the filepath to the hotel json file
     */
    public void processFiles(String file){
        Gson gson = new Gson();
        try(FileReader fr = new FileReader(file)){
            JsonObject jb = (JsonObject) JsonParser.parseReader(fr);
            JsonArray jsonArray = jb.getAsJsonArray("sr");

            List<Hotels> hotels = new ArrayList<>();

            for(JsonElement element: jsonArray){
                JsonObject hotelJson = element.getAsJsonObject();
                JsonObject ll = hotelJson.getAsJsonObject("ll");
                String latitude = (ll.get("lat").getAsString());
                String longitude = (ll.get("lng").getAsString());

                com.hotelapp.backend.datasetparser.entities.Hotel oldHotel = gson.fromJson(hotelJson, com.hotelapp.backend.datasetparser.entities.Hotel.class);
                oldHotel.setLatitude(latitude);
                oldHotel.setLongitude(longitude);

                hotels.add(HotelMapper.map(oldHotel));
            }

            importService.saveHotels(hotels);

        }catch(IOException e){
            System.out.println("Cannot open file: " + file + " - " + e.getMessage());
        }
    }
}
