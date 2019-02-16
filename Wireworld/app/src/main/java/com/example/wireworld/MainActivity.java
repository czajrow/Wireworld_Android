package com.example.wireworld;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.example.wireworld.logics.Matrix;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final String STATE_MATRIX = "matrix";

    private int dimension = 10;
    Matrix matrix = new Matrix(dimension);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SurfaceView surface = findViewById(R.id.surfaceView);
        setupSurfaceView(surface);
        setupButtons(surface);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putCharArray(STATE_MATRIX, matrix.toArray());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: in");
        super.onRestoreInstanceState(savedInstanceState);
        matrix = Matrix.fromArray(savedInstanceState.getCharArray(STATE_MATRIX));
        dimension = matrix.DIMENSION;
    }

    private void setupSurfaceView(final SurfaceView surface) {

        surface.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    float x = motionEvent.getX();
                    float y = motionEvent.getY();
                    int cellX = (int) map(x, 0, surface.getWidth(), 0, dimension);
                    int cellY = (int) map(y, 0, surface.getHeight(), 0, dimension);
                    matrix.nextState(cellX, cellY);
                    Canvas canvas = surface.getHolder().lockCanvas();
                    surface.getHolder().unlockCanvasAndPost(drawMatrix(canvas));
                }
                return true;
            }
        });

        surface.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas canvas = surface.getHolder().lockCanvas();
                surface.getHolder().unlockCanvasAndPost(drawMatrix(canvas));
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });
    }

    private void setupButtons(final SurfaceView surface) {
        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matrix.newGeneration();
                Canvas canvas = surface.getHolder().lockCanvas();
                surface.getHolder().unlockCanvasAndPost(drawMatrix(canvas));
            }
        });

        Button newButton = findViewById(R.id.newButton);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matrix = new Matrix(dimension);
                Canvas canvas = surface.getHolder().lockCanvas();
                surface.getHolder().unlockCanvasAndPost(drawMatrix(canvas));
            }
        });
    }

    private Canvas drawMatrix(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        int h = canvas.getHeight() / matrix.DIMENSION;
        int w = canvas.getWidth() / matrix.DIMENSION;
        for (int x = 0; x < matrix.DIMENSION; x++) {
            for (int y = 0; y < matrix.DIMENSION; y++) {
                float left = x * w;
                float top = y * h;
                float right = left + w;
                float bottom = top + h;
                Paint paint = matrix.getPaint(x, y);
                canvas.drawRect(left, top, right - 1, bottom - 1, paint);
            }
        }
        return canvas;
    }

    private double map(double value, double min1, double max1, double min2, double max2) {
        if (value <= min1) {
            return min2;
        } else if (value >= max1) {
            return max2;
        } else {
            return ((value - min1) * (max2 - min2)) / (max1 - min1) + min2;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: in");
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (dimension == 10) {
            menu.findItem(R.id.mnu10).setChecked(true);
        } else if (dimension == 15) {
            menu.findItem(R.id.mnu15).setChecked(true);
        } else {
            menu.findItem(R.id.mnu20).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.mnu10:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    dimension = 10;
                }
                break;
            case R.id.mnu15:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    dimension = 15;
                }
                break;
            case R.id.mnu20:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    dimension = 20;
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        matrix = new Matrix(dimension);
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        Canvas canvas = surfaceView.getHolder().lockCanvas();
        surfaceView.getHolder().unlockCanvasAndPost(drawMatrix(canvas));
        return true;
    }
}
