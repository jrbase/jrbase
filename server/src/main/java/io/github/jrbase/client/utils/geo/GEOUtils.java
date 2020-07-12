package io.github.jrbase.client.utils.geo;

import static java.lang.Math.*;

/**
 * geohash utils
 */
public class GEOUtils {

    private static final double EARTH_RADIUS_IN_METERS = 6372797.560856; // m

    static double D_R = (Math.PI / 180.0);

    // 1. GEOADD key longitude latitude member [longitude latitude member ...]
    //    geoadd store all position to skipList
    // 2. GEODIST key member1 member2 [m|km|ft|mi]
    public static double extractUnit(String unit) {
        // 1 * 1000 m = km
        // 1 * 0.3048 m = ft
        // 1 * 1609.34 m = mi
        if ("m".equals(unit)) {
            return 1;
        } else if ("km".equals(unit)) {
            return 1000;
        } else if ("ft".equals(unit)) {
            return 0.3048;
        } else if ("mi".equals(unit)) {
            return 1609.34;
        } else {
            throw new RuntimeException("(error) ERR unsupported unit provided. please use m, km, ft, mi");
        }
    }

    public static String distanceByUnit(double lng1, double lat1, double lng2, double lat2, String unit) {
        double result = getDistance(lng1, lat1, lng2, lat2) / extractUnit(unit);
        return String.format("%.4f", result);
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    static double getDistance(double lng1, double lat1, double lng2, double lat2) {
        double lat1r = rad(lat1);
        double lon1r = rad(lng1);
        double lat2r = rad(lat2);
        double lon2r = rad(lng2);

        double u = sin((lat2r - lat1r) / 2);
        double v = sin((lon2r - lon1r) / 2);

        return 2.0 * EARTH_RADIUS_IN_METERS *
                Math.asin(sqrt(u * u + cos(lat1r) * cos(lat2r) * v * v));
    }

}
