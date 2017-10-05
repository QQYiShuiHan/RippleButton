package com.example.hqq.ripplebutton.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatButton;
import android.view.MotionEvent;

/**
 * Created by HQQ on 2017/10/5.
 */

public class RippleButton extends AppCompatButton {


    private RippleDrawable rippleDrawable;


    public RippleButton(Context context) {
        this(context, null);
    }

    public RippleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        rippleDrawable = new RippleDrawable();
        rippleDrawable.setCallback(this);

        rippleDrawable.setRippleColor(0x30000000);
        rippleDrawable.setBackgroundColor(0x30000000);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 设置drawable绘制和重绘的区域
        rippleDrawable.setBounds(0, 0, getWidth(), getHeight());
    }


    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        // 验证drawable
        return who == rippleDrawable || super.verifyDrawable(who);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        rippleDrawable.onTouchChange(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rippleDrawable.draw(canvas);
        super.onDraw(canvas);
    }
}
