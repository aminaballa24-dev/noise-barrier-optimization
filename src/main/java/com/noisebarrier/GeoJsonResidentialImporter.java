package com.noisebarrier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GeoJsonResidentialImporter {

    private final ObjectMapper mapper = new ObjectMapper();

    public List<ResidentialArea> loadResidentialAreas(
            Path file, double fallbackLevels) throws IOException {

        if (file == null) {
            throw new IllegalArgumentException("File path cannot be null.");
        }

        if (!Double.isFinite(fallbackLevels) || fallbackLevels <= 0) {
            throw new IllegalArgumentException("Fallback levels must be positive.");
        }

        JsonNode root = mapper.readTree(file.toFile());
        JsonNode features = root.get("features");

        if (features == null || !features.isArray()) {
            throw new IllegalArgumentException("GeoJSON must contain features.");
        }

        List<ResidentialArea> areas = new ArrayList<>();

        int number = 0;

        for (JsonNode feature : features) {
            JsonNode geometry = feature.get("geometry");

            if (geometry == null
                    || !"Polygon".equals(geometry.path("type").asText())) {
                continue;
            }

            JsonNode ring = geometry.path("coordinates").path(0);

            if (!ring.isArray() || ring.size() < 3) {
                continue;
            }

            double[] x = new double[ring.size()];
            double[] y = new double[ring.size()];

            for (int i = 0; i < ring.size(); i++) {
                double longitude = ring.get(i).get(0).asDouble();
                double latitude = ring.get(i).get(1).asDouble();

                x[i] = LocalCoordinateConverter.toX(longitude);
                y[i] = LocalCoordinateConverter.toY(latitude);
            }

            double area = Geometry.polygonArea(x, y);

            if (area <= 0) {
                continue;
            }

            double[] centroid = Geometry.polygonCentroid(x, y);

            JsonNode properties = feature.path("properties");

            String id = properties.path("@id").asText("");

            if (id.isBlank()) {
                id = "residential-" + number;
            }

            double levels = readLevels(properties, fallbackLevels);
            double weight = area * levels;

            areas.add(new ResidentialArea(
                    id,
                    centroid[0],
                    centroid[1],
                    weight
            ));

            number++;
        }

        if (areas.isEmpty()) {
            throw new IllegalArgumentException(
                    "No residential buildings were found.");
        }

        return areas;
    }

    private double readLevels(JsonNode properties, double fallbackLevels) {
        String value = properties.path("building:levels").asText("");

        if (value.isBlank()) {
            return fallbackLevels;
        }

        try {
            double levels = Double.parseDouble(value);

            if (Double.isFinite(levels) && levels > 0) {
                return levels;
            }
        } catch (NumberFormatException ignored) {
        }

        return fallbackLevels;
    }
}