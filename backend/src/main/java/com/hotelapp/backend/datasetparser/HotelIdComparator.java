package com.hotelapp.backend.datasetparser;

import java.util.Comparator;

/**
 * HotelIdComparator class compares the String hotelId between two hotels, ensuring hotels are sorted by hotelid in
 * "increasing" alphabetical order
 */
public class HotelIdComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o1.compareTo(o2);
    }
}
