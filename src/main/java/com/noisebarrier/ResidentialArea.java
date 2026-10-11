package com.noisebarrier;

public class ResidentialArea {
    private String id;
    private double x;
    private double y;
    private int population;

    public ResidentialArea(String id, double x, double y, int population) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.population = population;
    }
    public int getPopulation() {
        return population;
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
