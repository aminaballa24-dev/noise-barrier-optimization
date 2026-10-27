package com.noisebarrier;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeoJsonResidentialImporterTest {

    @Test
    void loadsBucharestResidentialAreas() throws Exception {
        GeoJsonResidentialImporter importer =
                new GeoJsonResidentialImporter();

        List<ResidentialArea> areas =
                importer.loadResidentialAreas(
                        Path.of("bucharest.residential.geojson"),
                        1.0
                );

        assertEquals(547, areas.size());
        assertTrue(areas.get(0).getWeight() > 0);
    }
}