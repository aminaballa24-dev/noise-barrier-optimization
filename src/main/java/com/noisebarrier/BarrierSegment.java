package com.noisebarrier;

public class BarrierSegment {
    private final String id;
    private final double startX;
    private final double startY;
    private final double endX;
    private final double endY;
    private final double cost;

    public BarrierSegment(
            String id,
            double startX,
            double startY,
            double endX,
            double endY,
            double cost) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be empty.");
        }

        if (!Double.isFinite(startX) || !Double.isFinite(startY)
                || !Double.isFinite(endX) || !Double.isFinite(endY)) {
            throw new IllegalArgumentException("Coordinates must be finite.");
        }

        if (startX == endX && startY == endY) {
            throw new IllegalArgumentException(
                    "A barrier must have a non-zero length.");
        }

        if (!Double.isFinite(cost) || cost <= 0) {
            throw new IllegalArgumentException(
                    "Cost must be positive and finite.");
        }

        this.id = id;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getEndX() {
        return endX;
    }

    public double getEndY() {
        return endY;
    }

    public double getCost() {
        return cost;
    }

    public double getLength() {
        return Math.hypot(endX - startX, endY - startY);
    }
}

