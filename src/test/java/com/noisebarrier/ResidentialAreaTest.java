package com.noisebarrier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResidentialAreaTest {

    @Test
    void residentialAreaStoresCorrectValues() {
        ResidentialArea area = new ResidentialArea("A1", 5, 8, 120);

        assertEquals(5, area.getX());
        assertEquals(8, area.getY());
        assertEquals(120, area.getPopulation());
    }
}