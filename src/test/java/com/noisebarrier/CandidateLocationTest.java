package com.noisebarrier;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CandidateLocationTest {
    @Test
    void candidateLocationStoresCorrectValues() {
        CandidateLocation location = new CandidateLocation("L1", 5, 8);

        assertEquals("L1", location.getId());
        assertEquals(5.0, location.getX());
        assertEquals(8.0, location.getY());

    }
}
