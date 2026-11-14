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