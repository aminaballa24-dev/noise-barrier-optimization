package com.noisebarrier;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Path;
import java.util.List;

public class GeoJsonRoadImporterTest {

    @Test
    void loadsRoadsFromGeoJson() throws Exception {
        GeoJsonRoadImporter importer = new GeoJsonRoadImporter();

        List<Road> roads = importer.loadRoads(
                Path.of("bucharest.roads.geojson"));

        assertFalse(roads.isEmpty());

        assertTrue(roads.size() > 100);

        for (Road road : roads) {
            assertTrue(road.getLength() > 0);
        }
    }
}
