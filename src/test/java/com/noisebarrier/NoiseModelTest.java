package com.noisebarrier;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoiseModelTest {

    @Test
    void combinesTwoEqualNoiseLevelsCorrectly() {
        double result = NoiseModel.combineNoiseLevels(
                List.of(60.0, 60.0)
        );

        assertEquals(63.01, result, 0.02);
    }
}