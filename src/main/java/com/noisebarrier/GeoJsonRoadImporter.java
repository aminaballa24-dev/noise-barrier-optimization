package com.noisebarrier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class GeoJsonRoadImporter {

    private static final double CENTER_LON = 26.0875;
    private static final double CENTER_LAT = 44.452;
    private static final double REFERENCE_DISTANCE = 10.0;


    private final ObjectMapper mapper = new ObjectMapper();


    public List<Road> loadRoads(Path file) throws IOException {
        JsonNode root = mapper.readTree(file.toFile());
        JsonNode features = root.path("features");

        if (!features.isArray()) {
            throw new IOException(
                    "The file does not contain a GeoJSON features array.");
        }

        List<Road> roads = new ArrayList<>();

        for (JsonNode feature : features) {
            JsonNode properties = feature.path("properties");
            JsonNode geometry = feature.path("geometry");

            String highway = properties.path("highway").asText(
                    properties.path("tags").path("highway")
                            .asText("unclassified"));

            String roadId = properties.path("@id").asText(
                    properties.path("id").asText(
                            "road-" + roads.size()));

            double referenceNoiseDb =
                    referenceNoiseForHighway(highway);

            JsonNode coordinates = geometry.path("coordinates");

            if (!"LineString".equals(
                    geometry.path("type").asText())
                    || !coordinates.isArray()
                    || coordinates.size() < 2) {
                continue;
            }

            for (int i = 0; i < coordinates.size() - 1; i++) {
                JsonNode first = coordinates.get(i);
                JsonNode second = coordinates.get(i + 1);

                double lon1 = first.get(0).asDouble();
                double lat1 = first.get(1).asDouble();
                double lon2 = second.get(0).asDouble();
                double lat2 = second.get(1).asDouble();

                double x1 = toX(lon1);
                double y1 = toY(lat1);
                double x2 = toX(lon2);
                double y2 = toY(lat2);

                double length = Math.hypot(x2 - x1, y2 - y1);

                if (length < 0.5) {
                    continue;
                }

                // Approximate the energy contribution of a segment
                // relative to a 10-metre reference segment.
                double segmentNoiseDb = referenceNoiseDb
                        + 10.0 * Math.log10(
                        length / REFERENCE_DISTANCE);

                roads.add(new Road(
                        roadId + "-segment-" + i,
                        x1, y1, x2, y2,
                        segmentNoiseDb,
                        REFERENCE_DISTANCE));
            }
        }

        if (roads.isEmpty()) {
            throw new IOException(
                    "No usable road segments were found in the file.");
        }

        return roads;
    }

    private double toX(double longitude) {
        return (longitude - CENTER_LON)
                * 111320.0 * Math.cos(Math.toRadians(CENTER_LAT));
    }

        }
}
