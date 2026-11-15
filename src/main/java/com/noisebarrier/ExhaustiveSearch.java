
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


        for (int i = 0; i < candidates.size(); i++) {
            for (int j = i + 1; j < candidates.size(); j++) {

                BarrierSegment b1 = candidates.get(i);
                BarrierSegment b2 = candidates.get(j);

                if (b1.getLength() + b2.getLength() > maxLength) {
                    continue;
                }

                List<BarrierSegment> pair = List.of(b1, b2);

                double benefit = evaluator.calculateBenefit(
                        roads, areas, scenario, pair, barrierReductionDb);

                if (benefit > bestBenefit) {
                    bestBenefit = benefit;
                    best = pair;
                }
            }
        }


        return best;
    }
}
