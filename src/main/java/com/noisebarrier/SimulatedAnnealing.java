package com.noisebarrier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatedAnnealing {

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
                || !Double.isFinite(barrierReductionDb) || barrierReductionDb < 0
                || candidates.contains(null)) {
            throw new IllegalArgumentException("Invalid inputs.");
        }

        List<BarrierSegment> current = new ArrayList<>();
        List<BarrierSegment> best = new ArrayList<>();

        if (candidates.isEmpty() || maxLength == 0) {
            return best;
        }

        double totalWeight = 0;
        for (ResidentialArea area : areas) {
            totalWeight += area.getWeight();
        }

        double currentBenefit = 0;
        double bestBenefit = 0;
        double temperature = 2.0;

        Random random = new Random(42);

        for (int step = 0; step < 300; step++) {
            List<BarrierSegment> trial = new ArrayList<>(current);

            BarrierSegment barrier =
                    candidates.get(random.nextInt(candidates.size()));

            if (trial.contains(barrier)) {
                trial.remove(barrier);
            } else {
                trial.add(barrier);

                if (!BarrierBudget.isWithinBudget(trial, maxLength)
                        && !current.isEmpty()) {
                    trial.remove(current.get(
                            random.nextInt(current.size())));
                }
            }

            if (!BarrierBudget.isWithinBudget(trial, maxLength)) {
                temperature *= 0.99;
                continue;
            }

            double benefit = evaluator.calculateBenefit(
                    roads, areas, scenario, trial, barrierReductionDb);

            double difference = (benefit - currentBenefit) / totalWeight;

            // Sometimes accept a worse solution to explore other options.
            if (difference >= 0
                    || random.nextDouble() < Math.exp(difference / temperature)) {
                current = trial;
                currentBenefit = benefit;
            }

