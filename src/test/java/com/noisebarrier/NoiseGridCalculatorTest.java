
package com.noisebarrier;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class NoiseGridCalculatorTest {

    @Test
    void calculateNoiseFillsEveryGridCell() {
        CityGrid grid = new CityGrid(2, 2, 10.0, 0.0, 0.0);

        Road road = new Road(
                "road-1",
                0.0, 0.0,
                100.0, 0.0,
                60.0,
                10.0
        );

        TrafficScenario scenario = new TrafficScenario(
                "normal",
                "Normal traffic",
                1.0
        );

        NoiseGridCalculator calculator = new NoiseGridCalculator();

        calculator.calculateNoise(
                grid,
                List.of(road),
                scenario,
                List.of(),
                10.0
        );

        for (int row = 0; row < grid.getRows(); row++) {
            for (int column = 0; column < grid.getColumns(); column++) {
                assertTrue(
                        Double.isFinite(grid.getNoiseLevel(row, column)),
                        "Every grid cell should contain a finite noise value."
                );
            }
        }

        assertTrue(grid.getNoiseLevel(0, 0) > 0);
        assertTrue(grid.getNoiseLevel(1, 1) > 0);
    }
}
