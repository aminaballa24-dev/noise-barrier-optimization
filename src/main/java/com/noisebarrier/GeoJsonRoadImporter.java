package com.noisebarrier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.nio.file.Path;
import java.util.List;

public class GeoJsonRoadImporter {

    private static final double CENTER_LON = 26.0875;
    private static final double CENTER_LAT = 44.452;
    private static final double REFERENCE_DISTANCE = 10.0;


    private final ObjectMapper mapper = new ObjectMapper();


    public List<Road> loadRoads(Path file) throwa IOExceptopm {
        JsonNode root = mapper.readTree(file.toFile());
        JsonNode features = root.path("features");

        if
    }
}
