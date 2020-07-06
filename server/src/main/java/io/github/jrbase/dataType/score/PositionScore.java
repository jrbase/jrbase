package io.github.jrbase.dataType.score;

import ch.hsr.geohash.GeoHash;
import org.jetbrains.annotations.NotNull;

public class PositionScore implements Comparable<PositionScore> {
    private final String positionCode;
    private double longitude;
    private double latitude;

    public PositionScore(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        GeoHash geoHash = GeoHash.withCharacterPrecision(latitude, longitude, 8);
        this.positionCode = geoHash.toBase32();
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public int compareTo(@NotNull PositionScore o) {
        // double to binaryCode
        return this.positionCode.compareTo(o.positionCode);
    }
}
