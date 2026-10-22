package com.noisebarrier;

import org.junit.jupiter.api.Test;

public class NoiseModelTest {

    private final NoiseModel model = new NoiseModel();

    @Test
    void distanceDoublingReducesLineSourceLevelByAboutThreeDb() {
        Road road = new Road("R1", 0, 0, 10, 0, 60, 10);
        ResidentialArea area = new residentialArea("A1", 5, 20, 100);
        TrafficScenario scenario = new TrafficScenario("S1", "Normal", 1.0);

        double level = model.estimateRoadContributionDb(road, area, scenario, Collections.emptyList(), 5.0);


    }
}
