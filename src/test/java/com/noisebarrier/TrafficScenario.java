package com.noisebarrier;

public class TrafficScenario {
    private String id;
    private String name;
    private double trafficMultiplier;

    public TrafficScenario(String id, String name, double trafficMultiplier) {
        this.id = id;
        this.name = name;
        this.trafficMultiplier = trafficMultiplier;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getTrafficMultiplier(){
        return trafficMultiplier;
    }
}

