package com.noisebarrier;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CandidateBarrierGeneratorTest {

    @Test
    void generatesBarrierTowardResidentialArea() {
        Road road = new Road("R1", 0, 0, 10, 0, 60, 10);
        ResidentialArea house =
                new ResidentialArea("H1", 5, 15, 1);

        CandidateBarrierGenerator generator =
                new CandidateBarrierGenerator();

        List<BarrierSegment> candidates =
                generator.generateCandidates(
                        List.of(road),
                        List.of(house),
                        3.0,
                        100.0
                );

        assertEquals(1, candidates.size());

        BarrierSegment barrier = candidates.get(0);

        assertEquals(10.0, barrier.getLength(), 0.000001);
        assertEquals(3.0, barrier.getStartY(), 0.000001);
        assertEquals(3.0, barrier.getEndY(), 0.000001);
    }
}
