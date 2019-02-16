package com.example.wireworld.logics;

import android.graphics.Paint;

public class Cell {
    private static final String TAG = "Cell";

    private State state;

    public Cell(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void nextState() {
        switch (state) {
            case EMPTY:
                state = State.HEAD;
                break;
            case HEAD:
                state = State.TAIL;
                break;
            case TAIL:
                state = State.CONDUCTOR;
                break;
            case CONDUCTOR:
                state = State.EMPTY;
                break;
        }
    }

    public Paint getPaint() {
        Paint paint = new Paint();
        switch (state) {
            case EMPTY:
                paint.setARGB(255, 0, 0, 0);
                break;
            case HEAD:
                paint.setARGB(255, 0, 0, 255);
                break;
            case TAIL:
                paint.setARGB(255, 255, 0, 0);
                break;
            case CONDUCTOR:
                paint.setARGB(255, 255, 255, 0);
                break;
        }
        return paint;
    }
}
