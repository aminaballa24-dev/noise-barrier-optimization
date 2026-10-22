
package com.noisebarrier;

import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        Path file = Path.of("bucharest.roads.geojson");

        GeoJsonRoadImporter importer = new GeoJsonRoadImporter();
        List<Road> roads = importer.loadRoads(file);

        System.out.println("Road segments imported: " + roads.size());

        for (int i = 0; i < Math.min(5, roads.size()); i++) {
            Road road = roads.get(i);

            System.out.println(
                    road.getId()
                            + " | length: "
                            + String.format("%.1f", road.getLength())
                            + " m | reference noise: "
                            + road.getReferenceNoiseDb()
                            + " dB");
        }
    }
}
