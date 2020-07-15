package com.example.pomodoro.ui.countdowntimer;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.pomodoro.R;

public class CircleProgressBar extends View {
    private Paint mBackPaint;
    private Paint mFrontPaint;
    private Paint mTextPaint;
    private int mFrontColor, mBackColor, mTextColor;
    private int mTextSize;

    private float mStrokeWidth;
    private float mHalfStrokeWidth = mStrokeWidth / 2;
    private float mRadius;

    private RectF mRect;
    private int mWidth;
    private int mHeight;

    private int mProgress;
    private int mMax;


    public CircleProgressBar(Context context) {
        super(context);
        initPaint();
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.circleProgressBar);
        this.mStrokeWidth = ta.getInteger(R.styleable.circleProgressBar_stroke_width, 20);
        this.mRadius = ta.getInteger(R.styleable.circleProgressBar_radius, 600);
        this.mFrontColor = ta.getColor(R.styleable.circleProgressBar_front_color, Color.BLUE);
        this.mBackColor = ta.getColor(R.styleable.circleProgressBar_background_color, Color.WHITE);
        this.mTextColor = ta.getColor(R.styleable.circleProgressBar_text_color, Color.BLUE);
        this.mTextSize = ta.getDimensionPixelSize(R.styleable.circleProgressBar_text_size, 60);
        this.mProgress = ta.getInteger(R.styleable.circleProgressBar_default_progress, 0);
        this.mMax = ta.getInteger(R.styleable.circleProgressBar_max, 100);
        //最后记得将TypedArray对象回收
        ta.recycle();

        initPaint();
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    // 完成画笔相关参数的初始化
    private void initPaint() {
        mBackPaint = new Paint();
        mBackPaint.setColor(mBackColor);
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStyle(Paint.Style.STROKE);
        mBackPaint.setStrokeWidth(mStrokeWidth);
        mFrontPaint = new Paint();
        mFrontPaint.setColor(mFrontColor);
        mFrontPaint.setAntiAlias(true);
        mFrontPaint.setStyle(Paint.Style.STROKE);
        mFrontPaint.setStrokeWidth(mStrokeWidth);
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(80);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    // 重写测量大小的onMeasure方法
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getRealSize(widthMeasureSpec);
        mHeight = getRealSize(heightMeasureSpec);
        int min = (mWidth < mHeight) ? mWidth : mHeight;
        mRadius = (min - mStrokeWidth) / 2;
        setMeasuredDimension(mWidth, mHeight);
    }

    // 重写绘制View的核心方法onDraw()
    @Override
    protected void onDraw(Canvas canvas) {
        initRect();
        float angle = mProgress / (float) mMax * 360;
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mBackPaint);
        canvas.drawArc(mRect, -90, angle, false, mFrontPaint);
        canvas.drawText(mProgress + "%", mWidth / 2 + mHalfStrokeWidth,
                mHeight * 4 / 5 + mHalfStrokeWidth, mTextPaint);
        invalidate();
    }

    private int getRealSize(int measureSpec) {
        int result = 1;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {
            //自己计算
            int demand = (int) (mRadius * 2 + mStrokeWidth);
            result = (demand > size) ? size : demand;   //
        } else {
            result = size;
        }
        return result;
    }

    private void initRect() {
        if (mRect == null) {
            mRect = new RectF();
            int viewSize = (int) (mRadius * 2);
            int left = (mWidth - viewSize) / 2;
            int top = (mHeight - viewSize) / 2;
            int right = left + viewSize;
            int bottom = top + viewSize;
            mRect.set(left, top, right, bottom);
        }
    }

    // interface of CircleProgressBar
    public void setProgress(int progress) {
        mProgress = progress;
    }

    public int getProgress() {
        return this.mProgress;
    }
}
