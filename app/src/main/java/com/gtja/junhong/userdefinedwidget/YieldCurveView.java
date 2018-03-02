package com.gtja.junhong.userdefinedwidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/**
 * Created by Jack Feng on 2018/2/27.
 */

public class YieldCurveView extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    private SurfaceHolder mSurfaceHolder;

    private Canvas mCanvas;

    private List<DataModel> mDataModels;


    private boolean isDrawing;

    private Paint mAxesPainter;

    private Paint mCurvePainter;

    private Paint mScalePainter;

    private final int MARGIN_LEFT = 150;
    private final int MARGIN_RIGHT = 20;
    private final int MARGIN_TOP = 100;
    private final int MARGIN_BOTTOM = 100;

    private int width;

    private int height;

    private int x = MARGIN_LEFT;

    public YieldCurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);

        painterInit();
    }

    private void painterInit() {
        mAxesPainter = new Paint();
        mAxesPainter.setColor(Color.parseColor("#bdc9d6"));
        mAxesPainter.setStrokeWidth(1);
        mAxesPainter.setAntiAlias(true);

        mCurvePainter = new Paint();
        mCurvePainter.setColor(Color.parseColor("#6c9bd2"));
        mCurvePainter.setStrokeWidth(2);

        mScalePainter = new Paint();
        mScalePainter.setColor(Color.parseColor("#999999"));
        mScalePainter.setTextSize(35);
    }

    public void setData(List<DataModel> dataModels) {
        if (dataModels != null && dataModels.size() != 0) {
            this.mDataModels = dataModels;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        isDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isDrawing = false;
    }

    @Override
    public void run() {
        draw();
    }

    private void draw() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);

            width = getWidth();
            height = getHeight();

            drawAxes(mCanvas);
            drawCurve(mCanvas);
        } catch (Exception e) {

        } finally {
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private void drawAxes(Canvas canvas) {
        int countSpaceX = 5;
        int countSpaceY = 4;


        String[] yieldRateX = new String[] {
                "15.00%",
                "10.00%",
                "5.00%",
                "0.00%",
                "-5.00%%"
        };

        int stepX = (width - MARGIN_LEFT - MARGIN_RIGHT) / countSpaceX;
        int stepY = (height - MARGIN_TOP - MARGIN_BOTTOM) / countSpaceY;

        /**
         * 画竖线
         */
        for (int i = 0; i <= countSpaceX; i++) {
            canvas.drawLine(MARGIN_LEFT + stepX * i, MARGIN_TOP, MARGIN_LEFT + stepX * i, height - MARGIN_BOTTOM, mAxesPainter);
        }

        /**
         * 画横线
         */
        for (int k = 0; k <= countSpaceY; k++) {
            canvas.drawLine(MARGIN_LEFT, MARGIN_TOP + k * stepY, width - MARGIN_RIGHT, MARGIN_TOP + k * stepY, mAxesPainter);
            canvas.drawText(yieldRateX[k], 20, MARGIN_TOP + k * stepY + 10, mScalePainter);
        }
    }

    private void drawCurve(Canvas canvas) {
        int centerY = height / 2;
        while (x < width - MARGIN_RIGHT) {
            double rad = x * Math.PI / 180;
            int y = (int) (centerY - Math.sin(rad) * 100);
            canvas.drawPoint(x, y, mCurvePainter);
            x++;
        }
    }

    public void redraw() {
        invalidate();
    }
}
