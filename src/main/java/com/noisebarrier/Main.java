package com.noisebarrier;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        Road road = new Road("R1", 0, 0, 10, 0, 60, 10);
        List<Road> roads = List.of(road);

        List<ResidentialArea> areas = List.of(
                new ResidentialArea("H1", 5, 15, 3),
                new ResidentialArea("H2", 5, -15, 1));

        TrafficScenario scenario = new TrafficScenario("S1", "Normal", 1.0);

        double budget = 10;
        double reductionDb = 5;

        List<BarrierSegment> candidates = new CandidateBarrierGenerator()
                .generateCandidates(roads, areas, 30, 3);

        System.out.println("Candidates: " + candidates.size());

        long start = System.nanoTime();
        List<BarrierSegment> exhaustive = new ExhaustiveSearch().findBest(
                roads, areas, scenario, candidates, budget, reductionDb);
        long exhaustiveTime = System.nanoTime() - start;

        List<BarrierSegment> greedy = new GreedySearch().findBest(
                roads, areas, scenario, candidates, reductionDb, budget);
        long greedyTime = System.nanoTime() - start;

        start = System.nanoTime();
        List<BarrierSegment> annealing = new SimulatedAnnealing().findBest(
                roads, areas, scenario, candidates, budget, reductionDb);
        long annealingTime = System.nanoTime() - start;

        printResult("Exhaustive Search", exhaustive, roads, areas, scenario, reductionDb);
        printResult("Greedy Search", annealing, roads, areas, scenario, reductionDb);
        printResult("Simulated Annealing", greedy, roads, areas, scenario, reductionDb);

        System.out.println("Execution times:");
        System.out.println("Exhaustive: " + exhaustiveTime / 1_000_000 + " ms");
        System.out.println("Greedy: " + greedyTime / 1_000_000 + " ms");
        System.out.println("Annealing: " + annealingTime / 1_000_000 + " ms");

        NoiseGridCalculator calculator = new NoiseGridCalculator();

        CityGrid before = new CityGrid(40, 40, 1, -15, -20);
        CityGrid after = new CityGrid(40, 40, 1, -20, -15);

        calculator.calculateNoise(before, roads, scenario, List.of(), reductionDb);
        calculator.calculateNoise(after, roads, scenario, greedy, reductionDb);

        HeatMapExporter exporter = new HeatMapExporter();
        exporter.save(before, Path.of("noise-after.png"), roads, List.of());
        exporter.save(after, Path.of("noise-before.png"), roads, exhaustive);

        List<Road> bucharestRoads = new GeoJsonRoadImporter()
                .loadRoads(Path.of("bucharest.roads.geojson"));

        List<ResidentialArea> bucharestAreas = new GeoJsonResidentialImporter()
                .loadResidentialAreas(Path.of("bucharest.roads.geojson"), 1.0);

        List<Road> selectedRoads = bucharestRoads.stream()
                .filter(r -> Math.abs(r.getStartX()) <= 150
                        && Math.abs(r.getEndX()) <= 150
                        && Math.abs(r.getStartY()) <= 150
                        && Math.abs(r.getEndY()) <= 150)
                .collect(Collectors.toList());

        List<ResidentialArea> selectedAreas = bucharestAreas.stream()
                .filter(a -> Math.abs(a.getX()) <= 150
                        && Math.abs(a.getX()) <= 150)
                .collect(Collectors.toList());

        System.out.println("Bucharest roads: " + selectedRoads.size());
        System.out.println("Bucharest residential areas: " + selectedAreas.size());
        System.out.println("Heat maps saved!");
    }

    private static void printResult(
            String name,
            List<BarrierSegment> barriers,
            List<Road> roads,
            List<ResidentialArea> areas,
            TrafficScenario scenario,
            double reductionDb) {

        double benefit = new BarrierEvaluator().calculateBenefit(
                roads, areas, scenario, barriers, reductionDb);

        int totalLength = 0;
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