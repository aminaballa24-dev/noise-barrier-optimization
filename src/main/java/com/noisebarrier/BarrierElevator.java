package com.noisebarrier;

import java.util.List;

public class BarrierEvaluator {

    private final NoiseModel noiseModel = new NoiseModel();

    public double calculateBenefit(
            List<Road> roads,
            List<ResidentialArea> areas,
            TrafficScenario scenario,
            List<BarrierSegment> barriers,
            double barrierReductionDb) {

        if (roads == null || roads.isEmpty()
                || scenario == null || barriers == null) {
            throw new IllegalArgumentException("Invalid evaluation inputs.");
        }

