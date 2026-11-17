
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

        // Run all three algorithms.
        List<BarrierSegment> exhaustive =
                new ExhaustiveSearch().findBest(
                        roads, areas, scenario, candidates,
                        budget, reductionDb);

        List<BarrierSegment> greedy =
                new GreedySearch().findBest(
                        roads, areas, scenario, candidates,
                        budget, reductionDb);

        List<BarrierSegment> annealing =
                new SimulatedAnnealing().findBest(
                        roads, areas, scenario, candidates,
                        budget, reductionDb);

        // Compare their results.
        printResult("Exhaustive Search", exhaustive,
                roads, areas, scenario, reductionDb);

        printResult("Greedy Search", greedy,
                roads, areas, scenario, reductionDb);

        printResult("Simulated Annealing", annealing,
                roads, areas, scenario, reductionDb);
    }

