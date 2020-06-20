package com.example.retailsupporter.GPS;

import android.location.Location;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.retailsupporter.Activity.FragmentPages.TimeSheetFragment.STORE_NAME;

public class GPSLocation {

    static HashMap<String, ArrayList<Double>> storeInfo = new HashMap<String, ArrayList<Double>>();
    static ArrayList<Double> storeLocationMT, storeLocationFV, storeLocationMV, storeLocationAB;
    static Double LONGITUDE_MT = -122.0840000; //my house, testing
    static Double LATTUDE_MT = 37.421998333; //my house, testing
//    static Double LONGITUDE_MT = -122.999449;
//    static Double LATTUDE_MT = 49.225332;
    static Double LONGITUDE_AB = -123.133661;
    static Double LATTUDE_AB = 49.184002;
    static Double LONGITUDE_FV = -79.344460;
    static Double LATTUDE_FV = 43.778012;
    static Double LONGITUDE_MV = -79.286876;
    static Double LATTUDE_MV = 43.869447;

    static DecimalFormat df;

    private static void updateStoreLocation() {
        df = new DecimalFormat("#.000");
        df.format(LONGITUDE_MT);
        df.format(LATTUDE_MT);
        df.format(LONGITUDE_AB);
        df.format(LATTUDE_AB);
        df.format(LONGITUDE_FV);
        df.format(LATTUDE_FV);
        df.format(LONGITUDE_MV);
        df.format(LATTUDE_MV);

        storeLocationMT = new ArrayList<>();
        storeLocationMT.add(LONGITUDE_MT);
        storeLocationMT.add(LATTUDE_MT);
        storeInfo.put(STORE_NAME[1], storeLocationMT);

        storeLocationAB = new ArrayList<>();
        storeLocationAB.add(LONGITUDE_AB);
        storeLocationAB.add(LATTUDE_AB);
        storeInfo.put(STORE_NAME[2], storeLocationAB);

        storeLocationFV = new ArrayList<>();
        storeLocationFV.add(LONGITUDE_FV);
        storeLocationFV.add(LATTUDE_FV);
        storeInfo.put(STORE_NAME[3], storeLocationFV);

        storeLocationMV = new ArrayList<>();
        storeLocationMV.add(LONGITUDE_MV);
        storeLocationMV.add(LATTUDE_MV);
        storeInfo.put(STORE_NAME[4], storeLocationMV);
    }

    public static Boolean checkUserLocation(Location location, String userStore) {
        updateStoreLocation();
        Double longitude = location.getLongitude();
        Double latitude = location.getLatitude();
        df.format(longitude);
        df.format(latitude);

        Double StoreLong = storeInfo.get(userStore).get(0);
        Double StoreLati = storeInfo.get(userStore).get(1);

        if (df.format(longitude).equals(df.format(StoreLong)) && df.format(latitude).equals(df.format(StoreLati)) ) {
            return true;
        } else {
            return false;
        }

    }
}
