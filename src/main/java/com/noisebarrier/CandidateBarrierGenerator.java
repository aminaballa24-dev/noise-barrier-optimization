package com.noisebarrier;

import java.util.ArrayList;
import java.util.List;

public class CandidateBarrierGenerator {

    public List<BarrierSegment> generateCandidates(
            List<Road> roads,
            List<ResidentialArea> areas,
            double offset,
            double maxDistance) {

        if (roads == null || areas == null
                || !Double.isFinite(offset) || offset <= 0
                || !Double.isFinite(maxDistance) || maxDistance <= 0) {
            throw new IllegalArgumentException("Invalid generator inputs.");
        }

        List<BarrierSegment> candidates = new ArrayList<>();

        for (Road road : roads) {
            double dx = road.getEndX() - road.getStartX();
            double dy = road.getEndY() - road.getStartY();
            double length = road.getLength();

            // Perpendicular direction to the road.
            double nx = -dy / length;
            double ny = dx / length;

            boolean left = false;
            boolean right = false;

            for (ResidentialArea area : areas) {
                double px = area.getX() - road.getStartX();
                double py = area.getY() - road.getStartY();

                double t = (px * dx + py * dy) / (length * length);
                t = Math.max(0, Math.min(1, t));

                double closestX = road.getStartX() + t * dx;
                double closestY = road.getStartY() + t * dy;

                double distance = Math.hypot(
                        area.getX() - closestX,
                        area.getY() - closestY
                );

                if (distance > maxDistance) {
                    continue;
                }

                double cross = dx * py - dy * px;

                if (cross > 0) left = true;
                if (cross < 0) right = true;

                if (left && right) break;
            }

            if (left) {
                candidates.add(createBarrier(road, nx, ny, offset, "left"));
            }

            if (right) {
                candidates.add(createBarrier(road, -nx, -ny, offset, "right"));
            }
        }

        return candidates;
    }
