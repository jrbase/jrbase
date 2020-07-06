package io.github.jrbase.client.utils.geo;

import ch.hsr.geohash.GeoHash;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GEOUtilsTest {
    @Test
    public void testGeoHashJava() {
        double lat = 30.549608; // 纬度坐标
        double lon = 114.376971; // 经度坐标
        int precision = 8;
        GeoHash geoHash = GeoHash.withCharacterPrecision(lat, lon, precision);
        String binaryCode = geoHash.toBinaryString(); // 使用给定的经纬度坐标生成的二进制编码
        System.out.println("经纬度坐标： (" + lat + ", " + lon + ")");
        System.out.println("二进制编码：" + binaryCode);
        String hashCode = geoHash.toBase32(); // 使用给定的经纬度坐标生成的Geohash字符编码
        System.out.println("Geohash编码：" + hashCode);


        char[] binaryCodes = binaryCode.toCharArray();
        List<Character> latCodes = new ArrayList<>();
        List<Character> lonCodes = new ArrayList<>();
        for (int i = 0; i < binaryCodes.length; i++) {
            if (i % 2 == 0) {
                lonCodes.add(binaryCodes[i]);
            } else {
                latCodes.add(binaryCodes[i]);
            }
        }
        StringBuilder latCode = new StringBuilder(); // 纬度对应的二进制编码
        StringBuilder lonCode = new StringBuilder(); // 经度对应的二进制编码
        for (Character ch : latCodes) {
            latCode.append(ch);
        }
        for (Character ch : lonCodes) {
            lonCode.append(ch);
        }

        System.out.println("经度二进制编码：" + lonCode.toString());
        System.out.println("纬度二进制编码：" + latCode.toString());


    }

    @Test
    public void testDistance() {
        // 43.74948721127861
        double distance = GEOUtils.getDistance(102.485658, 25.270572, 102.828981, 24.887476);
        Assert.assertEquals(54880.3982, distance, 4);
    }

    @Test
    public void testDistanceByUnit() {
        // GEOADD Sicily 102.485658 25.270572 "Palermo" 102.828981 24.887476 "Catania"
        // geodist Sicily Palermo Catania
        String distance = GEOUtils.distanceByUnit(102.485658, 25.270572, 102.828981, 24.887476,
                "km");
        Assert.assertEquals("54.8804", distance);

        distance = GEOUtils.distanceByUnit(102.485658, 25.270572, 102.828981, 24.887476,
                "ft");
        Assert.assertEquals("180053.7998", distance);

        distance = GEOUtils.distanceByUnit(102.485658, 25.270572, 102.828981, 24.887476,
                "mi");
        Assert.assertEquals("34.1012", distance);

        distance = GEOUtils.distanceByUnit(102.485658, 25.270572, 102.828981, 24.887476,
                "m");
        Assert.assertEquals("54880.3982", distance);

    }


}
