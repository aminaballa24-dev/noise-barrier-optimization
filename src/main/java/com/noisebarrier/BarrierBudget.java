package com.noisebarrier;

import java.util.List;

public class BarrierBudget {

    public static boolean isWithinBudget(
            List<BarrierSegment> barriers,
            double maxLength) {

        if (barriers == null || !Double.isFinite(maxLength)
                || maxLength < 0) {
            throw new IllegalArgumentException("Invalid budget.");
        }

        double totalLength = 0;


