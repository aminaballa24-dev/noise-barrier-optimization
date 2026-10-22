package com.noisebarrier;
import java.util.ArrayList;
import java.util.List;


public class City {
    private List<Road> roads;
    private List<ResidentialArea> residentialAreas;
    private List<CandidateLocation> candidateLocations;

    public City() {
        roads = new ArrayList<>();
        residentialAreas = new ArrayList<>();
        candidateLocations = new ArrayList<>();
    }

    public void addRoad(Road road) {
        roads.add(road);
    }

    public void addCandidateLocation(CandidateLocation location) {
        candidateLocations.add(location);
    }
}
