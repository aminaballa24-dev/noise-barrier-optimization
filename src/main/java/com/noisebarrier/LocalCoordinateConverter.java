package com.noisebarrier;

public final class LocalCoordinateConverter {

    private static final double CENTER_LON = 26.0875;
    private static final double CENTER_LAT = 44.452;

    private LocalCoordinateConverter() {
    }

    public static double toX(double longitude) {
        if (!Double.isFinite(longitude)) {
            throw new IllegalArgumentException("Longitude must be finite.");
        }

        return (longitude - CENTER_LON)
                * 111320
                * Math.cos(Math.toRadians(CENTER_LAT));
    }

    public static double toY(double latitude) {
        if (!Double.isFinite(latitude)) {
            throw new IllegalArgumentException("Latitude must be finite.");
        }

        return (latitude - CENTER_LAT) * 110540;
    }
}