package com.noisebarrier;

import java.io.IOException;
import java.nio.file.Path;

public class HeatMapExporter {

    public void save(CityGrid grid, Path file) throws IOException {
        int pixelSize = 10;

        int width = grid.getRows() * pixelSize;
        int height = grid.getColumns() * pixelSize;

        BufferedImage image = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = image.createGraphics();

        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getColumns(); col++) {

                double noise = grid.getNoiseLevel(col, row);

                // Fixed color range: 40 to 80 dB.
                double value = (noise - 40) / 80;

                int red = (int) (255 * Math.min(1, 2 * value));
                int green = (int) (255 * Math.min(1, 2 * (1 - value)));