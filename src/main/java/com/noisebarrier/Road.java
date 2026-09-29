package com.noisebarrier;

public class Road {
    private String id;
    private double startX;
    private double startY;
    private double endX;
    private double endY;

    public Road(String id, double startX, double startY, double endX, double endY) {
        this.id = id;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }
    public double getLength() {
        double dx = endX - startX;
        double dy = endY - startY;

        return Math.sqrt(dx * dx + dy * dy);
    }
}
