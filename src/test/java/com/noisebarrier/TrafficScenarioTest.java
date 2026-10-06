package com.noisebarrier;

import org.junit.jupiter.api.Test;


public class TrafficScenarioTest {

    @Test
    void trafficScenarioStoresCorrectValues() {
        TrafficScenario = new TrafficScenario("T1", "Morning", 1.2);

        assertEquals("T1", scenario.getId());
        assertEquals("Morning", scenario.getName());
        assertEquals(1.2, scenario.getTrafficMuliplier());
    }
}
