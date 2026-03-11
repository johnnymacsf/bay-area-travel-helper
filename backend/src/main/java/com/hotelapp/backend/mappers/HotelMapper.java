package com.hotelapp.backend.mappers;

import com.hotelapp.backend.datasetparser.entities.Hotel;
import com.hotelapp.backend.entities.Hotels;

public class HotelMapper {
    public static Hotels map(Hotel oldHotel){
        Hotels entity = new Hotels();
        entity.setHotelId(oldHotel.getId());
        entity.setName(oldHotel.getHotelName());
        entity.setAddress(oldHotel.getAddress());
        entity.setCity(oldHotel.getCity());
        entity.setState(oldHotel.getState());
        entity.setLatitude(Double.valueOf(oldHotel.getLatitude()));
        entity.setLongitude(Double.valueOf(oldHotel.getLongitude()));
        return entity;
    }
}
