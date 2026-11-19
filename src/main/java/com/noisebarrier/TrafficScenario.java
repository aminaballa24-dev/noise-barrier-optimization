package com.noisebarrier;

public class TrafficScenario {
    private String id;
    private String name;
    private double trafficMultiplier;


    public TrafficScenario(String id, String name, double trafficMultiplier) {
        if (id == null || id.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("ID and name cannot be empty.");
        }

        if (!Double.isFinite(trafficMultiplier) || trafficMultiplier <= 0) {
            throw new IllegalArgumentException(
                    "Traffic multiplier must be positive and finite.");
        }

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

