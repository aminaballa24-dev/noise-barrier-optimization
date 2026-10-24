package com.noisebarrier;
import java.util.ArrayList;
import java.util.List;

public class NoiseModel {
    private static final double MIN_DISTANCE = 1.0;

    public double estimateRoadContributionDb(Road road, ResidentialArea area, TrafficScenario scenario, List<BarrierSegment> barriers, double barrierReductionDb) {
        if (road == null || area == null || scenario == null || barriers == null) {
            throw new IllegalArgumentException("Model inputs cannot be null.");
        }
        double multiplier = scenario.getTrafficMultiplier();

        if (!Double.isFinite(multiplier) || multiplier <= 0) {
            throw new IllegalArgumentException("Traffic multiplier must be positive and finite.");
        }

        if (!Double.isFinite(barrierReductionDb) || barrierReductionDb < 0) {
            throw new IllegalArgumentException("Barrier reduction must be non-negative and finite.");
        }

        double[] closestPoint = closestPointOnRoad(road, area.getX(), area.getY());

        double dx = area.getX() - closestPoint[0];
        double dy = area.getY() - closestPoint[1];

        double distance = Math.max(Math.hypot(dx, dy), MIN_DISTANCE);

        double distanceAdjustment = 10.0 * Math.log10(
                road.getReferenceDistance() / distance
        );

        double trafficAdjustment = 10.0 * Math.log10(multiplier);

        double noiseDb = road.getReferenceNoiseDb()
                + distanceAdjustment
                + trafficAdjustment;

        boolean blocked = false;

        for (BarrierSegment barrier : barriers) {
            if (Geometry.segmentIntersect(
                    closestPoint[0], closestPoint[1],
                    area.getX(), area.getY(),
                    barrier.getStartX(), barrier.getStartY(),
                    barrier.getEndX(), barrier.getEndY())) {
                blocked = true;
                break;
            }
        }

        if (blocked) {
            noiseDb -= barrierReductionDb;
        }

        return noiseDb;
    }

    public  double estimateCombinedNoiseDb(List<Road> roads, ResidentialArea area, TrafficScenario scenario, List<BarrierSegment> barriers, double barrierReductionDb) {

        if (roads == null || roads.isEmpty()) {
                throw new IllegalArgumentException("at least one road is required.");
        }

        List<Double> contributions = new ArrayList<>();

        for (Road road : roads) {
            contributions.add(estimateRoadContributionDb(road, area,scenario,barriers, barrierReductionDb));
        }

        return combineNoiseLevels(contributions);
    }

    public double estimateCombinedNoiseAtPointDb(
            List<Road> roads,
            double x,
            double y,
            TrafficScenario scenario,
            List<BarrierSegment> barriers,
            double barrierReductionDb) {

        ResidentialArea point = new ResidentialArea(
                "grid-point", x, y, 0);

        return estimateCombinedNoiseDb(
                roads, point, scenario, barriers, barrierReductionDb);
    }

    public static double combineNoiseLevels(List<Double> noiseLevelsDb) {

        if (noiseLevelsDb == null || noiseLevelsDb.isEmpty()) {
            throw new IllegalArgumentException("Noise levels must be finite.");
        }

        double energySum = 0.0;

        for (double level : noiseLevelsDb) {
            if (!Double.isFinite(level)) {
                throw new IllegalArgumentException("Noise levels must be finite.");
            }

            energySum += Math.pow(10.0, level / 10.0);
        }

        return 10.0 * Math.log10(energySum);
    }

    private double[] closestPointOnRoad(Road road, double x, double y) {
        double startX = road.getStartX();
        double startY = road.getStartY();

        double dx = road.getEndX() - startX;
        double dy = road.getEndY() - startY;

        double lengthSquared = dx * dx + dy * dy;

        double t = ((x - startX) * dx + (y - startY) * dy) / lengthSquared;

        t = Math.max(0.0, Math.min(1.0, t));

        return new double[] {
                startX + t * dx,
                startY + t * dy
        };
    }
}
