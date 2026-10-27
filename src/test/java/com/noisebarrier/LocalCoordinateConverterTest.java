package com.noisebarrier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocalCoordinateConverterTest {

    @Test
    void centerCoordinatesConvertToZero() {
        double x = LocalCoordinateConverter.toX(26.0875);
        double y = LocalCoordinateConverter.toY(44.452);

        assertEquals(0.0, x, 0.000001);
        assertEquals(0.0, y, 0.000001);
    }

    @Test
    void rejectsInvalidLongitude() {
        assertThrows(IllegalArgumentException.class, () ->
                LocalCoordinateConverter.toX(Double.NaN));
    }

    @Test
    void rejectsInvalidLatitude() {
        assertThrows(IllegalArgumentException.class, () ->
                LocalCoordinateConverter.toY(Double.NaN));
    }
}