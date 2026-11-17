package com.noisebarrier;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        runSyntheticComparison();
        runBucharestExample();
    }

    private static void runSyntheticComparison() {
        System.out.println("=== Small test scenario ===");

        List<Road> roads = List.of(
                new Road("R1", 0, 0, 10, 0, 60, 10));
        List<ResidentialArea> areas = List.of(
                new ResidentialArea("H1", 5, 15, 3),
                new ResidentialArea("H2", 5, -15, 1));

        TrafficScenario scenario = new TrafficScenario("S1", "Normal", 1.0);
        double budget = 10.0;
        double reductionDb = 5.0;

        CandidateBarrierGenerator generator = new CandidateBarrierGenerator();
        List<BarrierSegment> candidates =
                generator.generateCandidates(roads, areas, 3, 30);
        System.out.println("Candidates: " + candidates.size());

        long start = System.nanoTime();
        List<BarrierSegment> exhaustive = new ExhaustiveSearch().findBest(
                roads, areas, scenario, candidates, budget, reductionDb);
        long exhaustiveTime = System.nanoTime() - start;

        start = System.nanoTime();
        List<BarrierSegment> greedy = new GreedySearch().findBest(
                roads, areas, scenario, candidates, budget, reductionDb);
        long greedyTime = System.nanoTime() - start;

        start = System.nanoTime();
        List<BarrierSegment> annealing = new SimulatedAnnealing().findBest(
                roads, areas, scenario, candidates, budget, reductionDb);
        long annealingTime = System.nanoTime() - start;

        printResult("Exhaustive Search", exhaustive, roads, areas, scenario, reductionDb);
        printResult("Greedy Search", greedy, roads, areas, scenario, reductionDb);
        printResult("Simulated Annealing", annealing, roads, areas, scenario, reductionDb);

        System.out.println("Execution times:");
        System.out.println("Exhaustive: " + exhaustiveTime / 1_000_000.0 + " ms");
        System.out.println("Greedy: " + greedyTime / 1_000_000.0 + " ms");
        System.out.println("Simulated Annealing: " + annealingTime / 1_000_000.0 + " ms");
    }

    private static void runBucharestExample() throws IOException {
        System.out.println("\n=== Bucharest study area ===");

        GeoJsonRoadImporter roadImporter = new GeoJsonRoadImporter();
        GeoJsonResidentialImporter residentialImporter = new GeoJsonResidentialImporter();

        List<Road> allRoads = roadImporter.loadRoads(
                Path.of("bucharest.roads.geojson"));
        List<ResidentialArea> allAreas = residentialImporter.loadResidentialAreas(
                Path.of("bucharest.residential.geojson"), 1.0);

        // Keep the small, already-tested 300 x 300 m area for now.
        List<Road> roads = allRoads.stream()
                .filter(r -> Math.abs(r.getStartX()) <= 150
                        && Math.abs(r.getEndX()) <= 150
                        && Math.abs(r.getStartY()) <= 150
                        && Math.abs(r.getEndY()) <= 150)
                .collect(Collectors.toList());

        List<ResidentialArea> areas = allAreas.stream()
                .filter(a -> Math.abs(a.getX()) <= 150
                        && Math.abs(a.getY()) <= 150)
                .collect(Collectors.toList());

        System.out.println("Road segments: " + roads.size());
        System.out.println("Residential areas: " + areas.size());

        if (roads.isEmpty() || areas.isEmpty()) {
            throw new IllegalStateException("The study area must contain roads and residences.");
        }

        TrafficScenario scenario = new TrafficScenario("S1", "Normal", 1.0);
        double reductionDb = 5.0;
        double budget = 30.0;

        List<BarrierSegment> candidates = new CandidateBarrierGenerator()
                .generateCandidates(roads, areas, 3, 40);
        System.out.println("Candidates: " + candidates.size());

        List<BarrierSegment> selected = new GreedySearch().findBest(
                roads, areas, scenario, candidates, budget, reductionDb);
        printResult("Bucharest Greedy Search", selected, roads, areas, scenario, reductionDb);

        // Compare noise numerically using the 2D grid (no images).
        NoiseGridCalculator calculator = new NoiseGridCalculator();
        CityGrid before = new CityGrid(40, 40, 7.5, -150, -150);
        CityGrid after = new CityGrid(40, 40, 7.5, -150, -150);

        calculator.calculateNoise(before, roads, scenario, List.of(), reductionDb);
        calculator.calculateNoise(after, roads, scenario, selected, reductionDb);
        printGridComparison(before, after);
    }

    private static void printGridComparison(CityGrid before, CityGrid after) {
        double totalReduction = 0.0;
        double largestReduction = 0.0;
        int improvedCells = 0;
        int cellCount = before.getRows() * before.getColumns();

        for (int row = 0; row < before.getRows(); row++) {
            for (int col = 0; col < before.getColumns(); col++) {
                double reduction = before.getNoiseLevel(row, col)
                        - after.getNoiseLevel(row, col);
                totalReduction += reduction;
                largestReduction = Math.max(largestReduction, reduction);
                if (reduction > 1e-9) {
                    improvedCells++;
                }
            }
        }

        System.out.printf("Average grid-cell reduction: %.3f dB%n",
                totalReduction / cellCount);
        System.out.printf("Largest grid-cell reduction: %.3f dB%n", largestReduction);
        System.out.println("Grid cells improved: " + improvedCells + "/" + cellCount);
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

        double totalLength = 0.0;
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
