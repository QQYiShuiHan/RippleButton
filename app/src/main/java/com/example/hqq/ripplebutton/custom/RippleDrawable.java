package com.example.hqq.ripplebutton.custom;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;


/**
 * Created by HQQ on 2017/10/5.
 */

public class RippleDrawable extends Drawable {


    private Paint mPaint;
    /**
     * 绘制圆的半径
     */
    private float mRadius = 0;
    // 水波纹的中心
    private float mCircleX, mCircleY;
    // 水波纹画笔的透明度
    private float mAlpha = 50;
    // 水波纹画笔颜色
    private int mColor;
    // 这个drawable的中心
    private float mCenterX, mCenterY;
    // 最大半径
    private float mMaxRadius;
    // 背景颜色
    private int mBackgroundColor = 0x30000000;

    private int mBgChangeAlphaColor = 0;

    public RippleDrawable() {

        mPaint = new Paint();
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 抗抖动
        mPaint.setDither(true);
    }


    public void setBackgroundColor(int color) {
        this.mBackgroundColor = color;
    }

    public void onTouchChange(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:

                break;

            case MotionEvent.ACTION_UP:

                break;
        }
    }


    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        // 获取中心点的坐标
        mCenterX = bounds.centerX();
        mCenterY = bounds.centerY();
        // 获取最大半径
        mMaxRadius = Math.max(mCenterX, mCenterY);

    }


    protected int onColorChange(int backgroundColor, int alpha) {
        int a = (backgroundColor >> 24) & 0xff;
        a = (int) (a * (alpha / 255f));
        int r = (backgroundColor >> 16) & 0xff;
        int g = (backgroundColor >> 8) & 0xff;
        int b = backgroundColor & 0xff;

        return a << 24 | r << 16 | g << 8 | b;
    }

    protected void onTouchDown(float x, float y) {
        final float startX = x;
        final float startY = y;
        mRadius = 0;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setTarget(this);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();
                // 每次绘制的数据
                mRadius = mMaxRadius * fraction;
                mCircleX = startX + (mCenterX - startX) * fraction;
                mCircleY = startY + (mCenterY - startY) * fraction;
                // 计算透明度变化
                int changeAlpha = (int) (255 * fraction);
                // 改变透明后的颜色
                mBgChangeAlphaColor = onColorChange(mBackgroundColor, changeAlpha);

                invalidateSelf();
            }
        });
        valueAnimator.start();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        // 绘制背景逐渐加深
        canvas.drawColor(mBgChangeAlphaColor);
        // 绘制水波纹的效果
        canvas.drawCircle(mCircleX, mCircleY, mRadius, mPaint);
    }


    public void setRippleColor(int color) {
        this.mColor = color;
        onColorOrColorChange();
    }


    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int i) {
        this.mAlpha = i;
        onColorOrColorChange();
    }


    protected void onColorOrColorChange() {
        mPaint.setColor(mColor);
        if (mAlpha != 255) {
            int pAlpha = mPaint.getAlpha();
            mPaint.setAlpha((int) (pAlpha * (mAlpha / 255f)));
        }

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        if (mPaint.getColorFilter() != colorFilter) {
            mPaint.setColorFilter(colorFilter);
        }
    }

    @Override
    public int getOpacity() {
        int alpha = mPaint.getAlpha();
        if (alpha == 255) {
            return PixelFormat.OPAQUE;
        } else if (alpha == 0) {
            return PixelFormat.TRANSLUCENT;
        }
        return PixelFormat.TRANSLUCENT;
    }
}
