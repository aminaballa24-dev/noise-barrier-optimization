
package com.noisebarrier;

import java.util.List;
import java.util.ArrayList;

public class ExhaustiveSearch {

    private final BarrierEvaluator evaluator = new BarrierEvaluator();

    public List<BarrierSegment> findBest(
            List<Road> roads,
            List<ResidentialArea> areas,
            TrafficScenario scenario,
            List<BarrierSegment> candidates,
            double maxLength,
            double barrierReductionDb) {

        List<BarrierSegment> best = new ArrayList<>();
        double bestBenefit = 0;

        for (BarrierSegment barrier : candidates) {
            if (barrier.getLength() > maxLength) {
                continue;
            }

            double benefit = evaluator.calculateBenefit(
                    roads, areas, scenario,
                    List.of(barrier), barrierReductionDb);

            if (benefit > bestBenefit) {
                bestBenefit = benefit;
                best = List.of(barrier);
            }
        }

        return best;
    }
}
