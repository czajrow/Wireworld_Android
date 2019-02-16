package com.example.wireworld.logics;

import android.graphics.Paint;

import java.util.Random;

public class Matrix {
    private static final String TAG = "Matrix";

    private Cell[][] cells;
    public final int DIMENSION;

    public Matrix(int dimension) {
        this.DIMENSION = dimension;
        this.cells = new Cell[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                cells[i][j] = new Cell(State.EMPTY);
            }
        }
    }

    public void newGeneration() {
        Cell[][] newCells = new Cell[DIMENSION][DIMENSION];
        int x = DIMENSION, y = DIMENSION, x1, y1, x2, y2;
        for (y1 = 0; y1 < y; y1++) {                                // po kolumnach
            for (x1 = 0; x1 < x; x1++) {                            // po wierszach
                newCells[x1][y1] = new Cell(State.EMPTY);
                Cell cell = cells[x1][y1];
                switch (cell.getState()) {
                    case EMPTY:
                        newCells[x1][y1].setState(State.EMPTY);
                        break;
                    case HEAD:
                        newCells[x1][y1].setState(State.TAIL);
                        break;
                    case TAIL:
                        newCells[x1][y1].setState(State.CONDUCTOR);
                        break;
                    case CONDUCTOR:
                        int n = 0;
                        for (x2 = x1 - 1; x2 <= x1 + 1; x2++) {     // sasiednie kolumny
                            for (y2 = y1 - 1; y2 <= y1 + 1; y2++) { // sasiednie wiersze
                                if (cells[(x2 + x) % x][(y2 + y) % y].getState() == State.HEAD) {
                                    n++;
                                }
                            }
                        }
                        if (n < 1 || n > 2) {
                            newCells[x1][y1].setState(State.CONDUCTOR);
                        } else {
                            newCells[x1][y1].setState(State.HEAD);
                        }
                        break;
                }
            }
        }
        cells = newCells;
    }

    public Paint getPaint(int x, int y) {
        return cells[x][y].getPaint();
    }

    public void nextState(int x, int y) {
        cells[x][y].nextState();
    }

    public char[] toArray() {
        char[] arr = new char[DIMENSION * DIMENSION + 1];

        arr[0] = (char) DIMENSION;

        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                Cell cell = cells[i][j];
                switch (cell.getState()) {
                    case EMPTY:
                        arr[i * DIMENSION + j + 1] = 0;
                        break;
                    case HEAD:
                        arr[i * DIMENSION + j + 1] = 1;
                        break;
                    case TAIL:
                        arr[i * DIMENSION + j + 1] = 2;
                        break;
                    case CONDUCTOR:
                        arr[i * DIMENSION + j + 1] = 3;
                        break;
                }
            }
        }
        return arr;
    }

    public static Matrix fromArray(char[] arr) {
        Matrix matrix = new Matrix(arr[0]);
        Cell[][] cells = matrix.cells;
        int dimension = matrix.DIMENSION;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                Cell cell = cells[i][j];
                switch (arr[i * dimension + j + 1]) {
                    case 0:
                        cell.setState(State.EMPTY);
                        break;
                    case 1:
                        cell.setState(State.HEAD);
                        break;
                    case 2:
                        cell.setState(State.TAIL);
                        break;
                    case 3:
                        cell.setState(State.CONDUCTOR);
                        break;
                }
            }
        }
        return matrix;
    }
}
