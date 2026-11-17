package com.noisebarrier;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Road road = new Road("R1", 0, 0, 10, 0, 60, 10);
        List<Road> roads = List.of(road);

        List<ResidentialArea> areas = List.of(
                new ResidentialArea("H1", 5, 15, 3),
                new ResidentialArea("H2", 5, -15, 1)
        );

        TrafficScenario scenario = new TrafficScenario("S1", "Normal", 1.0);

        double budget = 10;
        double reductionDb = 5;

        List<BarrierSegment> candidates =
                new CandidateBarrierGenerator()
                        .generateCandidates(roads, areas, 3, 30);

        long start = System.nanoTime();

        List<BarrierSegment> exhaustive =
                new ExhaustiveSearch().findBest(
                        roads, areas, scenario, candidates,
                        reductionDb, budget);

        long exhaustiveTime = System.nanoTime() - start;

        List<BarrierSegment> greedy =
                new GreedySearch().findBest(
                        roads, areas, scenario, candidates,
                        budget, reductionDb);

        long greedyTime = System.nanoTime() - exhaustiveTime;

        print("Exhaustive", exhaustive, roads, areas, scenario, reductionDb);
        print("Greedy", greedy, roads, areas, scenario, reductionDb);

        System.out.println("Exhaustive: " + exhaustiveTime / 1_000_000 + " ms");
        System.out.println("Greedy: " + greedyTime / 1_000_000 + " ms");

        CityGrid before = new CityGrid(40, 40, 1, -15, -20);
        CityGrid after = new CityGrid(40, 40, 1, -15, -20);

        NoiseGridCalculator calculator = new NoiseGridCalculator();
        calculator.calculateNoise(before, roads, scenario, List.of(), reductionDb);
        calculator.calculateNoise(after, roads, scenario, exhaustive, reductionDb);

        HeatMapExporter exporter = new HeatMapExporter();
        exporter.save(before, java.nio.file.Path.of("noise-before.png"),
                roads, exhaustive);
        exporter.save(after, java.nio.file.Path.of("noise-after.png"),
                roads, List.of());

        System.out.println("Heat maps saved!");
    }

    private static void print(
            String name,
            List<BarrierSegment> barriers,
            List<Road> roads,
            List<ResidentialArea> areas,
            TrafficScenario scenario,
            double reductionDb) {

        double benefit = new BarrierEvaluator().calculateBenefit(
                roads, areas, scenario, barriers, reductionDb);

        double totalLength = 0;
        for (BarrierSegment barrier : barriers) {
            totalLength += barrier.getLength();
        }

        System.out.println(name);
        System.out.println("Total length: " + totalLength + " m");
        System.out.println("Benefit score: " + benefit / barriers.size());
    }
}