package com.noisebarrier;

public final class Geometry {

    private static final double EPSILON = 1e-9;

    private Geometry() {
    }

    public static boolean segmentIntersect(
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

    public static double polygonArea(double[] x, double[] y) {
        if (x == null || y == null || x.length != y.length || x.length < 3) {
            throw new IllegalArgumentException("A polygon needs at least 3 matching points.");
        }

        double sum = 0.0;

        for (int i = 0; i < x.length; i++) {
            int next = (i + 1) % x.length;

            sum += x[i] * y[next] - x[next] * y[i];
        }

        return Math.abs(sum) / 2.0;
    }

    public static double[] polygonCentroid(double[] x, double[] y) {
        if (x == null || y == null || x.length != y.length || x.length < 3) {
            throw new IllegalArgumentException("A polygon needs at least 3 matching points.");
        }

        double crossSum = 0.0;
        double centroidX = 0.0;
        double centroidY = 0.0;

        for (int i = 0; i < x.length; i++) {
            int next = (i + 1) % x.length;

            double cross = x[i] * y[next] - x[next] * y[i];

            crossSum += cross;
            centroidX += (x[i] + x[next]) * cross;
            centroidY += (y[i] + y[next]) * cross;
        }

        double signedArea = crossSum / 2.0;

        if (Math.abs(signedArea) < EPSILON) {
            throw new IllegalArgumentException("Polygon area cannot be zero.");
        }

        centroidX /= 6.0 * signedArea;
        centroidY /= 6.0 * signedArea;

        return new double[]{centroidX, centroidY};
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