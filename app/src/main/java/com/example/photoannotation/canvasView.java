package com.example.photoannotation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class canvasView extends View {

    public static int BRUSH_SIZE = 20;
//    public static int ERASER_SIZE = BRUSH_SIZE;
//    public static final int ERASER_COLOR = Color.TRANSPARENT;
    public static final int DEFAULT_COLOR = Color.RED;
    public static final int DEFAULT_BG_COLOR = Color.TRANSPARENT;
    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    public ArrayList<DrawnPath> paths = new ArrayList<>();
    public int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    public int strokeWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    public Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    public boolean isEraser = false;


    public canvasView(Context context) {
        this(context, null);
    }

    public canvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);
    }

    public void init(DisplayMetrics metrics) {
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;
    }

    public void clear() {

        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        paths.clear();
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        mCanvas.drawColor(backgroundColor);

        for (DrawnPath dp : paths) {
            mPaint.setColor(dp.color);
            mPaint.setStrokeWidth(dp.strokeWidth);
//            mPaint.setMaskFilter(null);

            mCanvas.drawPath(dp.path, mPaint);
        }
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }

    private void touchStart(float x, float y) {
        mPath = new Path();
        DrawnPath dp = new DrawnPath(currentColor, strokeWidth, mPath);
        paths.add(dp);

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);

            mCanvas.drawPath(mPath, mPaint);
            mPath.reset();
            mPath.moveTo(mX, mY);

            mX = x;
            mY = y;
        }
    }
    private void touchUp() {
        mPath.reset();
//        mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchStart(x, y);
            invalidate();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (isEraser) {

                touchMove(x, y);
                mPath.reset();
                mPath.moveTo(x, y);
                invalidate();

            } else {

                touchMove(x, y);
                invalidate();

            }
        } else { // MotionEvent.ACTION_UP
            touchUp();
            invalidate();
        }
        return true;
    }

    public void setEraser(boolean isEraser) {
        this.isEraser = isEraser;
        if (isEraser) {
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            mPaint.setXfermode(null);
        }
    }


}