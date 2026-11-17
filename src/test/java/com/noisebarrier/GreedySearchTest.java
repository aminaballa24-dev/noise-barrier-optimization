
package com.noisebarrier;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GreedySearchTest {

    @Test
    void selectsUsefulBarriersWithinBudget() {
        Road road = new Road("R1", 0, 0, 10, 0, 60, 10);

        ResidentialArea house1 =
                new ResidentialArea("H1", 5, 15, 2);
        ResidentialArea house2 =
                new ResidentialArea("H2", 5, -15, 2);

        TrafficScenario scenario =
                new TrafficScenario("S1", "Normal", 1);

        BarrierSegment b1 =
                new BarrierSegment("B1", 0, 3, 10, 3, 10);
        BarrierSegment b2 =
                new BarrierSegment("B2", 0, -3, 10, -3, 10);

        GreedySearch search = new GreedySearch();

        List<BarrierSegment> result = search.findBest(
                List.of(road),
                List.of(house1, house2),
                scenario,
                List.of(b1, b2),
                20,
                5
        );

        assertEquals(2, result.size());
        assertTrue(BarrierBudget.isWithinBudget(result, 20));
    }

    @Test
    void zeroBudgetSelectsNothing() {
        Road road = new Road("R1", 0, 0, 10, 0, 60, 10);
        ResidentialArea house =
                new ResidentialArea("H1", 5, 15, 2);

        TrafficScenario scenario =
                new TrafficScenario("S1", "Normal", 1);

        BarrierSegment barrier =
                new BarrierSegment("B1", 0, 3, 10, 3, 10);

        GreedySearch search = new GreedySearch();

        List<BarrierSegment> result = search.findBest(
                List.of(road),
                List.of(house),
                scenario,
                List.of(barrier),
                0,
                5
        );

        assertTrue(result.isEmpty());
    }
}
