
package com.noisebarrier;

import java.util.ArrayList;
import java.util.List;

public class ExhaustiveSearch extends BarrierOptimizer {

    @Override
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

        // Exhaustive search is only used for small candidate sets.
        if (candidates.size() > 15) {
            throw new IllegalArgumentException("Too many candidates.");
        }

        if (candidates.stream().anyMatch(b -> b == null)) {
            throw new IllegalArgumentException("Candidates cannot contain null.");
        }

        List<BarrierSegment> best = new ArrayList<>();
        double bestBenefit = 0;

        int totalCombinations = 1 << candidates.size();

        for (int mask = 1; mask < totalCombinations; mask++) {
            List<BarrierSegment> selected = new ArrayList<>();
            double totalLength = 0;

            // Build the current combination.
            for (int i = 0; i < candidates.size(); i++) {
                if ((mask & (1 << i)) != 0) {
                    BarrierSegment barrier = candidates.get(i);
                    selected.add(barrier);
                    totalLength += barrier.getLength();
                }
            }

            // Skip combinations that exceed the budget.
            if (totalLength > maxLength + 1e-9) {
                continue;
            }

            double benefit = evaluator.calculateBenefit(
                    roads, areas, scenario,
                    selected, barrierReductionDb);

            if (benefit > bestBenefit) {
                bestBenefit = benefit;
                best = new ArrayList<>(selected);
            }
        }

        return best;
    }
}
