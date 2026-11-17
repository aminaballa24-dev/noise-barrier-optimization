
package com.noisebarrier;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Create a small test scenario.
        Road road = new Road("R1", 0, 0, 10, 0, 60, 10);
        List<Road> roads = List.of(road);

        List<ResidentialArea> areas = List.of(
                new ResidentialArea("H1", 5, 15, 3),
                new ResidentialArea("H2", 5, -15, 1)
        );

        TrafficScenario scenario =
                new TrafficScenario("S1", "Normal", 1.0);

        double budget = 10;
        double reductionDb = 5;

        // Generate possible barriers.
        CandidateBarrierGenerator generator =
                new CandidateBarrierGenerator();

        List<BarrierSegment> candidates =
                generator.generateCandidates(roads, areas, 3, 30);

        System.out.println("Candidates: " + candidates.size());

        long start = System.nanoTime();

        // Run all three algorithms.
        List<BarrierSegment> exhaustive =
                new ExhaustiveSearch().findBest(
                        roads, areas, scenario, candidates,
                        budget, reductionDb);

        long exhaustiveTime = System.nanoTime() - start;

        start = System.nanoTime();

        List<BarrierSegment> greedy =
                new GreedySearch().findBest(
                        roads, areas, scenario, candidates,
                        budget, reductionDb);

        long greedyTime = System.nanoTime() - start;

        start = System.nanoTime();

        List<BarrierSegment> annealing =
                new SimulatedAnnealing().findBest(
                        roads, areas, scenario, candidates,
                        budget, reductionDb);

        long annealingTime = System.nanoTime() - start;

        // Compare their results.
        printResult("Exhaustive Search", exhaustive,
                roads, areas, scenario, reductionDb);

        printResult("Greedy Search", greedy,
                roads, areas, scenario, reductionDb);

        printResult("Simulated Annealing", annealing,
                roads, areas, scenario, reductionDb);

        System.out.println("Execution times:");
        System.out.println("Exhaustive: " + exhaustiveTime / 1_000_000.0 + " ms");
        System.out.println("Greedy: " + greedyTime / 1_000_000.0 + " ms");
        System.out.println("Simulated Annealing: " + annealingTime / 1_000_000.0 + " ms");
    }

    private static void printResult(
            String name,
            List<BarrierSegment> barriers,
            List<Road> roads,
            List<ResidentialArea> areas,
            TrafficScenario scenario,
            double reductionDb) {

        BarrierEvaluator evaluator = new BarrierEvaluator();

        double benefit = evaluator.calculateBenefit(
                roads, areas, scenario, barriers, reductionDb);

        double totalLength = 0;

        for (BarrierSegment barrier : barriers) {
            totalLength += barrier.getLength();
        }

        System.out.println(name);
        System.out.println("Selected barriers: " + barriers.size());
        System.out.println("Total length: " + totalLength + " m");
        System.out.println("Benefit score: " + benefit);
        System.out.println();
    }
}

