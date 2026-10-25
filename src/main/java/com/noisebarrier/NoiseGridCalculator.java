package com.noisebarrier;

import java.util.List;

public class NoiseGridCalculator {

    public void calculateNoise(
            CityGrid grid,
            List<Road> roads,
            TrafficScenario scenario,
            List<BarrierSegment> barriers,
            double barrierReductionDb) {

        NoiseModel noiseModel = new NoiseModel();

        for (int row = 0; row < grid.getRows(); row++) {
            for (int column = 0; column < grid.getColumns(); column++) {

                double x = grid.getX(column);
                double y = grid.getY(row);

                double noiseDb = noiseModel.estimateCombinedNoiseAtPointDb(
                        roads,
                        x,
                        y,
                        scenario,
                        barriers,
                        barrierReductionDb
                );

                grid.setNoiseLevel(row, column, noiseDb);
            }
        }
    }
}
