package com.noisebarrier;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrafficScenarioTest {

    @Test
    void trafficScenarioStoresCorrectValues() {
        TrafficScenario scenario = new TrafficScenario("T1", "Morning", 1.2);

        assertEquals("T1", scenario.getId());
        assertEquals("Morning", scenario.getName());
        assertEquals(1.2, scenario.getTrafficMultiplier());
    }
}
