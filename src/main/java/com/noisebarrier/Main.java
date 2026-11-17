package com.noisebarrier;

import java.util.List;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

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


// Calculate noise before and after adding barriers.
        NoiseGridCalculator calculator = new NoiseGridCalculator();

        CityGrid before = new CityGrid(40, 40, 1, -15, -20);
        CityGrid after = new CityGrid(40, 40, 1, -15, -20);

        calculator.calculateNoise(
                before, roads, scenario, List.of(), reductionDb);

        calculator.calculateNoise(
                after, roads, scenario, exhaustive, reductionDb);

// Save both heat maps.
        HeatMapExporter exporter = new HeatMapExporter();


        exporter.save(before, Path.of("noise-before.png"),
                roads, List.of());

        exporter.save(after, Path.of("noise-after.png"),
                roads, exhaustive);


        GeoJsonRoadImporter roadImporter = new GeoJsonRoadImporter();
        GeoJsonResidentialImporter residentialImporter =
                new GeoJsonResidentialImporter();

        List<Road> bucharestRoads =
                roadImporter.loadRoads(Path.of("bucharest.roads.geojson"));

        List<ResidentialArea> bucharestAreas =
                residentialImporter.loadResidentialAreas(
                        Path.of("bucharest.residential.geojson"), 1.0);

// Use a small area around our coordinate-system center.
        List<Road> selectedRoads = bucharestRoads.stream()
                .filter(r -> Math.abs(r.getStartX()) <= 150
                        && Math.abs(r.getEndX()) <= 150
                        && Math.abs(r.getStartY()) <= 150
                        && Math.abs(r.getEndY()) <= 150)
                .collect(Collectors.toList());

        List<ResidentialArea> selectedAreas = bucharestAreas.stream()
                .filter(a -> Math.abs(a.getX()) <= 150
                        && Math.abs(a.getY()) <= 150)
                .collect(Collectors.toList());

        System.out.println("Bucharest roads: " + selectedRoads.size());
        System.out.println("Bucharest residential areas: " + selectedAreas.size());


        CityGrid bucharestBefore = new CityGrid(40, 40, 7.5, -150, -150);

        calculator.calculateNoise(
                bucharestBefore, selectedRoads, scenario, List.of(), reductionDb);

        exporter.save(bucharestBefore, Path.of("bucharest-before.png"),
                selectedRoads, List.of());

        System.out.println("Bucharest baseline heat map saved!");

        List<BarrierSegment> bucharestCandidates =
                generator.generateCandidates(
                        selectedRoads, selectedAreas, 3, 40);

        System.out.println("Bucharest candidates: "
                + bucharestCandidates.size());


        double bucharestBudget = 30.0;

// Select barriers using Greedy Search.
        List<BarrierSegment> bucharestSelected =
                new GreedySearch().findBest(
                        selectedRoads, selectedAreas, scenario,
                        bucharestCandidates, bucharestBudget, reductionDb);

        printResult("Bucharest Greedy Search", bucharestSelected,
                selectedRoads, selectedAreas, scenario, reductionDb);

// Calculate noise after placing barriers.
        CityGrid bucharestAfter = new CityGrid(40, 40, 7.5, -150, -150);

        calculator.calculateNoise(
                bucharestAfter, selectedRoads, scenario,
                bucharestSelected, reductionDb);

// Export the second heat map.
        exporter.save(bucharestAfter, Path.of("bucharest-after.png"),
                selectedRoads, bucharestSelected);

        System.out.println("Bucharest after heat map saved!");


        for (CityGrid grid : List.of(bucharestBefore, bucharestAfter)) {
            double minNoise = Double.POSITIVE_INFINITY;
            double maxNoise = Double.NEGATIVE_INFINITY;

            for (int row = 0; row < grid.getRows(); row++) {
                for (int col = 0; col < grid.getColumns(); col++) {
                    double noise = grid.getNoiseLevel(row, col);
                    minNoise = Math.min(minNoise, noise);
                    maxNoise = Math.max(maxNoise, noise);
                }
            }

            System.out.println("Noise range: " + minNoise
                    + " to " + maxNoise + " dB");
        }




        System.out.println("Heat maps saved!");



        Road wholeRoad = new Road("Whole", 0, 0, 10, 0, 60, 10);

        Road firstHalf = new Road("Half1", 0, 0, 5, 0, 60, 10);
        Road secondHalf = new Road("Half2", 5, 0, 10, 0, 60, 10);

        CityGrid wholeGrid = new CityGrid(1, 1, 1, 4.5, 14.5);
        CityGrid splitGrid = new CityGrid(1, 1, 1, 4.5, 14.5);

        calculator.calculateNoise(
                wholeGrid, List.of(wholeRoad), scenario,
                List.of(), reductionDb);

        calculator.calculateNoise(
                splitGrid, List.of(firstHalf, secondHalf), scenario,
                List.of(), reductionDb);

        double wholeNoise = wholeGrid.getNoiseLevel(0, 0);
        double splitNoise = splitGrid.getNoiseLevel(0, 0);

        System.out.println("Whole road noise: " + wholeNoise);
        System.out.println("Split road noise: " + splitNoise);
        System.out.println("Difference: " + (splitNoise - wholeNoise));


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