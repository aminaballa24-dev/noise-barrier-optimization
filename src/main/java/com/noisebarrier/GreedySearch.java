package com.noisebarrier;

import java.util.ArrayList;
import java.util.List;

public class GreedySearch {

    private final BarrierEvaluator evaluator = new BarrierEvaluator();

    public List<BarrierSegment> findBest(
            List<Road> roads,
            List<ResidentialArea> areas,
            TrafficScenario scenario,
            List<BarrierSegment> candidates,
            double maxLength,
            double barrierReductionDb) {

        if (roads == null || roads.isEmpty()
                || areas == null || areas.isEmpty()
                || scenario == null || candidates == null
                || !Double.isFinite(maxLength) || maxLength < 0
                || !Double.isFinite(barrierReductionDb) || barrierReductionDb < 0) {
            throw new IllegalArgumentException("Invalid search inputs.");
        }

        if (candidates.stream().anyMatch(b -> b == null)) {
            throw new IllegalArgumentException("Invalid candidate.");
        }

        List<BarrierSegment> selected = new ArrayList<>();
        double usedLength = 0;
        double currentBenefit = 0;