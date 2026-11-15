
package com.noisebarrier;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BarrierEvaluatorTest {

    @Test
    void calculatesBenefitOfBarrier() {

        Road road = new Road("R1", 0, 0, 10, 0, 60, 10);
        ResidentialArea house =
                new ResidentialArea("H1", 5, 15, 2.0);

        TrafficScenario scenario =
                new TrafficScenario("S1", "Normal", 1.0);

        BarrierSegment barrier =
                new BarrierSegment("B1", 0, 3, 10, 3, 10);

        BarrierEvaluator evaluator = new BarrierEvaluator();

        double benefit = evaluator.calculateBenefit(
                List.of(road),
                List.of(house),
                scenario,
                List.of(barrier),
                5.0
        );

        // 5 dB reduction multiplied by residential weight 2.
        assertEquals(10.0, benefit, 0.000001);
    }

    @Test
    void noBarriersGiveZeroBenefit() {

        Road road = new Road("R1", 0, 0, 10, 0, 60, 10);
        ResidentialArea house =
                new ResidentialArea("H1", 5, 15, 2.0);

        TrafficScenario scenario =
                new TrafficScenario("S1", "Normal", 1.0);

        BarrierEvaluator evaluator = new BarrierEvaluator();

        double benefit = evaluator.calculateBenefit(
                List.of(road),
                List.of(house),
                scenario,
                List.of(),
                5.0
        );

        assertEquals(0.0, benefit, 0.000001);
    }
}
