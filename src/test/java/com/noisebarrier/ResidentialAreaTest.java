package com.noisebarrier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResidentialAreaTest {

    @Test
    void residentialAreaStoresCorrectValues() {
        ResidentialArea area = new ResidentialArea("A1", 5, 8, 120.5);

        assertEquals("A1", area.getId());
        assertEquals(5, area.getX());
        assertEquals(8, area.getY());
        assertEquals(120.5, area.getWeight());
    }

    @Test
    void rejectsBlankId() {
        assertThrows(IllegalArgumentException.class, () ->
                new ResidentialArea("", 5, 8, 120.5));
    }

    @Test
    void rejectsInvalidCoordinates() {
        assertThrows(IllegalArgumentException.class, () ->
                new ResidentialArea("A1", Double.NaN, 8, 120.5));
    }

    @Test
    void rejectsInvalidWeight() {
        assertThrows(IllegalArgumentException.class, () ->
                new ResidentialArea("A1", 5, 8, 0));
    }
}