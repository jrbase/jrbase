package io.github.jrbase.client.utils.geo;

/**
 * geohash utils
 */
public class GEOUtils {
    private static final double EARTH_RADIUS = 6371;// km

    public static void add(double latitude, double longitude) {
//        SkipList<GeoObj> geoObjSkipList = new SkipList();
    }
    // TODO:
    // 1. GEOADD key longitude latitude member [longitude latitude member ...]
    //    geoadd store all position to skipList
    // 2. GEODIST key member1 member2 [m|km|ft|mi]
    //

    public static String distanceByUnit(double lat1, double lng1, double lat2, double lng2, String unit) {
        double result;
        if ("km".equals(unit)) {
            result = distance(lat1, lng1, lat2, lng2);
        } else if ("m".equals(unit)) {
            // km = 1000m
            result = distance(lat1, lng1, lat2, lng2) * 1000;
        } else if ("ft".equals(unit)) {
            // 1 m	3.28 ft
            result = distance(lat1, lng1, lat2, lng2) * 1000 * 3.28;
        } else if ("mi".equals(unit)) {
            // 1km = 0.62137mi
            result = distance(lat1, lng1, lat2, lng2) * 0.62137;
        } else {
            throw new RuntimeException("(error) ERR unsupported unit provided. please use m, km, ft, mi");
        }
        return String.format("%.4f", result);
    }

    /**
     * distance
     *
     * @return (km)
     */
    static double distance(double lat1, double lng1, double lat2, double lng2) {
        double x1 = Math.cos(lat1) * Math.cos(lng1);
        double y1 = Math.cos(lat1) * Math.sin(lng1);
        double z1 = Math.sin(lat1);
        double x2 = Math.cos(lat2) * Math.cos(lng2);
        double y2 = Math.cos(lat2) * Math.sin(lng2);
        double z2 = Math.sin(lat2);
        double lineDistance =
                Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
        return EARTH_RADIUS * Math.PI * 2 * Math.asin(0.5 * lineDistance) / 180;
    }

}
