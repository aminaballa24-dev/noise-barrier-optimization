package com.noisebarrier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoadTest {
    @Test
    void getLength_returnsCorrectLength() {
        Road road = new Road("R1", 0, 0, 3, 4);

        assertEquals(5.0, road.getLength());
    }

}