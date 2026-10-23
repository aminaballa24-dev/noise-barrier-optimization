package com.noisebarrier;

public final class Geometry {

    private static final double EPSILON = 1e-9;

    private Geometry() {
    }

    public static boolean segmentsIntersect(
            double x1, double y1,
            double x2, double y2,
            double x3, double y3,
            double x4, double y4) {

        int o1 = orientation(x1, y1, x2, y2, x3, y3);
        int o2 = orientation(x1, y1, x2, y2, x4, y4);
        int o3 = orientation(x3, y3, x4, y4, x1, y1);
        int o4 = orientation(x3, y3, x4, y4, x2, y2);

        // The segments cross each other.
        if (o1 != o2 && o3 != o4) {
            return true;
        }

        // Handle collinear segments and endpoints.
        if (o1 == 0 && onSegment(x1, y1, x3, y3, x2, y2)) {
            return true;
        }

        if (o2 == 0 && onSegment(x1, y1, x4, y4, x2, y2)) {
            return true;
        }

        if (o3 == 0 && onSegment(x3, y3, x1, y1, x4, y4)) {
            return true;
        }

        if (o4 == 0 && onSegment(x3, y3, x2, y2, x4, y4)) {
            return true;
        }

        return false;
    }

    private static int orientation(
            double ax, double ay,
            double bx, double by,
            double cx, double cy) {

        double value =
                (by - ay) * (cx - bx)
                        - (bx - ax) * (cy - by);

        if (Math.abs(value) < EPSILON) {
            return 0;
        }

        return value > 0 ? 1 : 2;
    }

    private static boolean onSegment(
            double ax, double ay,
            double px, double py,
            double bx, double by) {

        return px >= Math.min(ax, bx) - EPSILON
                && px <= Math.max(ax, bx) + EPSILON
                && py >= Math.min(ay, by) - EPSILON
                && py <= Math.max(ay, by) + EPSILON;
    }
}