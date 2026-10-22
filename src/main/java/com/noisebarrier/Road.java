package com.noisebarrier;

public class Road {
    private final String id;
    private final double startX;
    private final double startY;
    private final double endX;
    private final double endY;
    private final double referenceNoiseDb;
    private final double referenceDistance;

    public Road(
            String id,
            double startX,
            double startY,
            double endX,
            double endY,
            double referenceNoiseDb,
            double referenceDistance) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Road ID cannot be empty.");
        }

        if (!Double.isFinite(startX) || !Double.isFinite(startY)
                || !Double.isFinite(endX) || !Double.isFinite(endY)) {
            throw new IllegalArgumentException("Coordinates must be finite.");
        }

        if (startX == endX && startY == endY) {
            throw new IllegalArgumentException(
                    "A road must have a non-zero length.");
        }

        if (!Double.isFinite(referenceNoiseDb)) {
            throw new IllegalArgumentException(
                    "Reference noise level must be finite.");
        }

        if (!Double.isFinite(referenceDistance) || referenceDistance <= 0) {
            throw new IllegalArgumentException("Reference distance must be positive and finite.");
        }

        this.id = id;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.referenceNoiseDb = referenceNoiseDb;
        this.referenceDistance = referenceDistance;
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

    public double getReferenceNoiseDb() {
        return referenceNoiseDb;
    }

    public double getReferenceDistance() {
        return referenceDistance;
    }

    public double getLength() {
        return Math.hypot(endX - startX, endY - startY);
    }
}
