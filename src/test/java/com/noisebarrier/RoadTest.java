package com.noisebarrier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

    class RoadTest {

        @Test
        void roadStoresCorrectValues() {
            Road road = new Road("R1", 0, 0, 3, 4, 60, 10);


            assertEquals("R1", road.getId());
            assertEquals(0.0, road.getStartX());
            assertEquals(0.0, road.getStartY());
            assertEquals(3.0, road.getEndX());
            assertEquals(4.0, road.getEndY());
            assertEquals(60.0, road.getReferenceNoiseDb());
            assertEquals(10.0, road.getReferenceDistance());
            assertEquals(5.0, road.getLength());

        }

        @Test
        void roadRejectsZeroLength() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Road("R1", 2, 2, 2, 2, 60, 10));
        }
    }

