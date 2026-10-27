package com.noisebarrier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class GeometryTest {

    @Test
    void intersectingSegmentsReturnTrue() {
        boolean result = Geometry.segmentIntersect(
                0, 0,
                10, 10,
                0, 10,
                10, 0
        );

        assertTrue(result);
    }

    @Test
    void separateSegmentsReturnFalse() {
        boolean result = Geometry.segmentIntersect(
                0, 0,
                5, 0,
                0, 5,
                5, 5
        );

        assertFalse(result);
    }

    @Test
    void touchingSegmentsReturnTrue() {
        boolean result = Geometry.segmentIntersect(
                0, 0,
                5, 0,
                5, 0,
                10, 0
        );

        assertTrue(result);
    }

    @Test
    void calculatesAreaOfSquare() {
        double[] x = {0, 10, 10, 0};
        double[] y = {0, 0, 10, 10};

        double area = Geometry.polygonArea(x, y);

        assertEquals(100.0, area);
    }

    @Test
    void calculatesCentroidOfSquare() {
        double[] x = {0, 10, 10, 0};
        double[] y = {0, 0, 10, 10};

        double[] centroid = Geometry.polygonCentroid(x, y);

        assertEquals(5.0, centroid[0]);
        assertEquals(5.0, centroid[1]);
    }

    @Test
    void calculatesCentroidOfIrregularPolygon() {
        double[] x = {0, 4, 4, 1, 1, 0};
        double[] y = {0, 0, 1, 1, 4, 4};

        double[] centroid = Geometry.polygonCentroid(x, y);

        assertEquals(1.357142857, centroid[0], 0.000001);
        assertEquals(1.357142857, centroid[1], 0.000001);
    }
}