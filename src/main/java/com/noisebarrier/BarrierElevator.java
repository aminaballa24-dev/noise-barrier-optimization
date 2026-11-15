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

        double totalBenefit = 0.0;

        for (ResidentialArea area : areas) {

            double before = noiseModel.estimateCombinedNoiseDb(
                    roads, area, scenario, List.of(), barrierReductionDb);

            double after = noiseModel.estimateCombinedNoiseDb(
                    roads, area, scenario, barriers, barrierReductionDb);

            double reduction = before - after;

            totalBenefit += area.getWeight() * reduction;
        }

        return totalBenefit;
    }
}

