package com.hotelapp.backend.datasetparser.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Hotel class used to create Hotel objects from the hotels json file, serialized name to map json values to their
 * respective class fields
 */
public class Hotel {
    @SerializedName("id")
    private String id;
    @SerializedName("f")
    private String hotelName;
    @SerializedName("ad")
    private String address;
    @SerializedName("ci")
    private String city;

    @SerializedName("pr")
    private String hotelState;

    private String latitude;
    private String longitude;

    /**
     * Hotel class constructor
     * @param id is hotel Id
     * @param hotelName is the name of the hotel
     * @param address is the hotel address
     * @param city is the hotel city
     * @param latitude is the latitude of the hotel
     * @param longitude is the longitude of the hotel
     */
    public Hotel(String id, String hotelName, String address, String city, String hotelState, String latitude, String longitude){
        this.id = id;
        this.hotelName = hotelName;
        this.address = address;
        this.city = city;
        this.hotelState = hotelState;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Getter for the hotel id
     * @return the hotelId
     */
    public String getId(){
        return id;
    }

    /**
     * setter for latitude
     * @param latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * setter for longitude
     * @param longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude(){
        return latitude;
    }

    public String getHotelName(){
        return hotelName;
    }

    public String getAddress(){
        return address;
    }
    public String getCity(){
        return city;
    }
    public String getState(){
        return hotelState; }

    /**
     * toString() method for printing out hotel class fields
     * @return a string of the hotel information for easy print out
     */
    @Override
    public String toString() {
        return "hotelName = " + hotelName + System.lineSeparator()
                + "hotelId = " + id + System.lineSeparator()
                + "latitude = " + latitude + System.lineSeparator()
                + "longitude = " + longitude + System.lineSeparator()
                + "address = " + address + ", " + city;
    }

    /**
     * organizes query output
     * @return
     */
    public String queryOutput(){
        StringBuilder sb = new StringBuilder();
        sb.append(hotelName).append(": ").append(id).append(System.lineSeparator());
        sb.append(address).append(System.lineSeparator());
        sb.append(city).append(", ").append(hotelState).append(System.lineSeparator());
        return sb.toString();
    }
}
