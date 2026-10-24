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

    public void addResidentialArea(ResidentialArea area) {
        residentialAreas.add(area);
    }

    public void addCandidateLocation(CandidateLocation location) {
        candidateLocations.add(location);
    }

    public List<Road> getRoads() {
        return roads;
    }

    public List<ResidentialArea> getResidentialAreas() {
        return residentialAreas;
    }

    public List<CandidateLocation> getCandidateLocations() {
        return candidateLocations;
    }


}
