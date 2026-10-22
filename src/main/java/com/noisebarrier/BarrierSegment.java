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
}
