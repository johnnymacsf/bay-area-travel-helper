package com.hotelapp.backend.datasetparser.repos;

import com.hotelapp.backend.datasetparser.HotelIdComparator;
import com.hotelapp.backend.datasetparser.entities.Hotel;


import java.util.TreeMap;

/**
 * Hotel Repository class stores the hashmap of all hotels which are parsed from the json file and contains methods
 * for adding and retrieving hotels.
 */
public class HotelRepository {
    private final TreeMap<String, Hotel> hotelMap;

    public HotelRepository(){
        this.hotelMap = new TreeMap<>(new HotelIdComparator());
    }

    public void addHotel(Hotel hotel){
        hotelMap.put(hotel.getId(), hotel);
    }

    /**
     * getHotel returns a hotel with the matching parameter hotel id
     * @param id represents the id of a hotel we want to return
     * @return the hotel with the hotel id matching the parameter id
     */
    public Hotel getHotel(String id){
        return hotelMap.get(id);
    }

    /**
     * getAllHotels returns a copy of the hashmap of all the hotels
     * @return a copy of the hashmap of all the hotels in hotelMap
     */
    public TreeMap<String, Hotel> getAllHotels(){
        return new TreeMap<>(hotelMap);
    }
}
