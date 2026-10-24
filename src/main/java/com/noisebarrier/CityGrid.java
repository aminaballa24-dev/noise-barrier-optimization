package com.noisebarrier;

public class CityGrid {

    private final int rows;
    private final int columns;
    private final double cellSize;
    private final double[][] noiseLevels;
    private final double originX;
    private final double originY;


    public CityGrid(int rows, int columns, double cellSize,
                    double originX, double originY) {
        if (rows <= 0 || columns <= 0 || cellSize <= 0) {
            throw new IllegalArgumentException("Grid dimensions and cell size must be positive.");
        }

        this.rows = rows;
        this.columns = columns;
        this.cellSize = cellSize;
        this.originX = originX;
        this.originY = originY;

        this.noiseLevels = new double[rows][columns];
    }

    public void setNoiseLevel(int row, int column, double noiseDb) {
        if (row < 0 || row >= rows || column < 0 || column >= columns) {
            throw new IndexOutOfBoundsException("Grid position is outside the grid.");
        }

        if (!Double.isFinite(noiseDb)) {
            throw new IllegalArgumentException("Noise level must be finite.");
        }

        noiseLevels[row][column] = noiseDb;
    }

    public double getNoiseLevel(int row, int column) {
        if (row < 0 || row >= rows || column < 0 || column >= columns) {
            throw new IndexOutOfBoundsException("Grid position is outside the grid.");
        }

        return noiseLevels[row][column];
    }

    public double getX(int column) {
        if (column < 0 || column >= columns) {
            throw new IndexOutOfBoundsException("Column is outside the grid.");
        }

        return originX + (column + 0.5) * cellSize;
    }

    public double getY(int row) {
        if (row < 0 || row >= rows) {
            throw new IndexOutOfBoundsException("Row is outside the grid.");
        }

        return originY + (row + 0.5) * cellSize;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public double getCellSize() {
        return cellSize;
    }
}
