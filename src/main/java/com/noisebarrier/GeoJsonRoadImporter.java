package com.noisebarrier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;

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

                int pieces = (int) Math.ceil(
                        length / REFERENCE_DISTANCE);

                double pieceLength = length / pieces;

                double pieceNoiseDb = referenceNoiseDb
                        + 10.0 * Math.log10(
                        pieceLength / REFERENCE_DISTANCE);

                for (int j = 0; j < pieces; j++) {
                    double t0 = j / (double) pieces;
                    double t1 = (j + 1) / (double) pieces;

                    double pieceStartX = x1 + t0 * (x2 - x1);
                    double pieceStartY = y1 + t0 * (y2 - y1);
                    double pieceEndX = x1 + t1 * (x2 - x1);
                    double pieceEndY = y1 + t1 * (y2 - y1);

                    roads.add(new Road(
                            roadId + "-segment-" + i + "-part-" + j,
                            pieceStartX,
                            pieceStartY,
                            pieceEndX,
                            pieceEndY,
                            pieceNoiseDb,
                            REFERENCE_DISTANCE));
                }
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

        private double toY(double latitude) {
            return (latitude - CENTER_LAT) * 110540.0;
        }

        private double referenceNoiseForHighway(String highway) {
            // Illustrative assumptions, NOT measured noise data.
            switch (highway) {
                case "primary":
                    return 70.0;
                case "secondary":
                    return 67.0;
                case "tertiary":
                    return 64.0;
                case "unclassified":
                    return 62.0;
                case "residential":
                    return 60.0;
                case "living_street":
                    return 55.0;
                default:
                    return 60.0;
            }
        }
    }
