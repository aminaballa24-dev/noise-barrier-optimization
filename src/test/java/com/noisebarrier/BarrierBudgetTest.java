package com.noisebarrier;

import org.junit.jupiter.api.Test;

class BarrierBudgetTest {

    @Test
    void acceptsBarriersWithinBudget() {
        BarrierSegment b1 = new BarrierSegment("B1", 0, 0, 10, 0, 10);
        BarrierSegment b2 = new BarrierSegment("B2", 0, 5, 20, 5, 20);

        assertTrue(BarrierBudget.isWithinBudget(
                List.of(b1, b2), 40));
    }

    @Test
    void rejectsBarriersOverBudget() {
        BarrierSegment b1 = new BarrierSegment("B1", 0, 0, 10, 0, 10);
        BarrierSegment b2 = new BarrierSegment("B2", 0, 5, 20, 5, 20);

        assertFalse(BarrierBudget.isWithinBudget(
                List.of(b1, b2), 25));
    }
}