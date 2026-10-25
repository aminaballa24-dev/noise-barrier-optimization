package com.noisebarrier;

public class ResidentialArea {
    private String id;
    private double x;
    private double y;
    private double weight;

    public ResidentialArea(String id, double x, double y, double weight) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Residential area id cannot be blank.");
        }

        if (!Double.isFinite(x) || !Double.isFinite(y)) {
            throw new IllegalArgumentException("Coordinates must be finite.");
        }

        if (!Double.isFinite(weight) || weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive and finite.");
        }

        this.id = id;
        this.x = x;
        this.y = y;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public double getWeight() {
        return weight;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}